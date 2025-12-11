package dev.jtbw.aoc2025.meta

import dev.jtbw.aoc2025.d
import dev.jtbw.aoc2025.i
import java.io.File
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Client for interacting with the Advent of Code API.
 *
 * This client handles authentication and caching for Advent of Code puzzle inputs and answer
 * submissions.
 *
 * ## Authentication
 *
 * The session token is automatically read from AOC_SESSION environment variable or
 * `~/.aoc/session_token`.
 *
 * ## Caching
 *
 * Puzzle inputs are cached locally in `~/.aoc/{year}/{day}.txt`. When [fetchInput] is called, the
 * cache is checked first. If the file exists, it is returned immediately without making an API
 * call. If not, the input is fetched from the API and cached for future use.
 *
 * Correct answers are cached in `~/.aoc/{year}/answer{part}.txt`. When [checkAnswer] is called, if
 * the cached answer matches the provided answer, true is returned immediately. If the submission
 * succeeds (HTTP 200), the answer is cached for future calls.
 *
 * @throws IllegalStateException if `~/.aoc/session_token` does not exist
 */
class AdventOfCodeClient : AutoCloseable {
  private val service: AdventOfCodeService
  private val sessionToken: String
  private val client = OkHttpClient.Builder().build()

  init {
    val sessionFile = File("src/main/resources/session_token")

    sessionToken =
      System.getenv("AOC_SESSION")
        ?: run {
          if (sessionFile.exists()) {
            sessionFile.readText().trim()
          } else {
            throw IllegalStateException(
              "Session token not found. Please set AOC_SESSION or create ${sessionFile.absolutePath}"
            )
          }
        }

    val retrofit =
      Retrofit.Builder()
        .baseUrl("https://adventofcode.com/")
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    service = retrofit.create(AdventOfCodeService::class.java)
  }

  suspend fun fetchInput(year: Int, day: Int): String {
    val cacheFile = DataFiles.realInput(day)
    if (cacheFile.exists()) {
      return cacheFile.readText().trimEnd()
    }

    // Fetch from API
    val input = service.getInput(year, day, "session=$sessionToken").trimEnd()

    return input.also { cacheFile.writeText(it) }
  }

  suspend fun checkAnswer(answer: String, year: Int, day: Int, part: Int): AnswerResponse {
    val cacheFile: File = DataFiles.realAnswer(day, part)
    val knownAnswer = cacheFile.takeIf { it.exists() }?.readText()
    if (knownAnswer != null) {
      return if (answer == knownAnswer) {
        AnswerResponse.Correct
      } else {
        AnswerResponse.Wrong(expected = knownAnswer, actual = answer)
      }
    }

    // Submit to API
    val response = service.submitAnswer(year, day, part, answer, "session=$sessionToken")
    val body = response.body() ?: ""

    val result =
      when {
        AnswerResponse.Correct.matchString in body -> AnswerResponse.Correct
        AnswerResponse.Wrong.matchString in body -> {
          val expected =
            if ("too high" in body) {
              "Too high!"
            } else if ("too low" in body) {
              "Too low!"
            } else {
              "Unknown"
            }
          AnswerResponse.Wrong(expected = expected, actual = answer)
        }
        AnswerResponse.RateLimit.matchString in body -> {
          val time = Regex("You have (.*?) left to wait").find(body)?.groupValues[1]
          AnswerResponse.RateLimit(time)
        }
        AnswerResponse.OldLevel.matchString in body -> AnswerResponse.OldLevel
        else -> {
          d(body)
          error("No matching AnswerResponse for")
        }
      }

    i("Result = $result")

    // Cache if successful
    if (result == AnswerResponse.Correct) {
      cacheFile.writeText(answer)
    }

    return result
  }

  override fun close() {
    client.dispatcher.executorService.shutdown()
    client.connectionPool.evictAll()
  }
}

sealed interface AnswerResponse {

  object Correct : AnswerResponse {
    val matchString: String = "That's the right answer!"
  }

  data class Wrong(val expected: String?, val actual: String) : AnswerResponse {
    companion object {
      val matchString: String = "That's not the right answer"
    }
  }

  data class RateLimit(val timeLeft: String?) : AnswerResponse {
    companion object {
      val matchString: String = "You gave an answer too recently"
    }
  }

  object OldLevel : AnswerResponse {
    val matchString: String = "You don't seem to be solving the right level"
  }
}
