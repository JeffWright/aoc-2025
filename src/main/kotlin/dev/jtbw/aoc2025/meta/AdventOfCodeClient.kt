package dev.jtbw.aoc2025.meta

import dev.jtbw.aoc2025.e
import dev.jtbw.aoc2025.i
import java.io.File
import java.io.FileNotFoundException
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

  suspend fun fetchInput(data: InputAndAnswer): String {
    data.getInput()?.let {
      return it
    }

    // Fetch from API
    val input = service.getInput(data.year, data.day, "session=$sessionToken")

    return input.also { data.recordInput(it) }
  }

  suspend fun checkAnswer(data: InputAndAnswer, answer: String): AnswerResponse {
    val knownAnswer = data.getCorrectAnswer()
    if (knownAnswer != null) {
      return if (answer == knownAnswer) {
        AnswerResponse.Correct
      } else {
        AnswerResponse.Wrong
      }
    }

    if (!data.primary) {
      throw FileNotFoundException("No such file: ${data.answerFile.absolutePath}")
    }

    // Submit to API
    val response =
      service.submitAnswer(data.year, data.day, data.part, answer, "session=$sessionToken")
    val body = response.body() ?: ""

    val result =
      AnswerResponse.entries.firstOrNull { it.string in body }
        ?: run {
          i(body)
          error("No matching AnswerResponse")
        }

    i("Result = $result")

    when (result) {
      AnswerResponse.Correct,
      AnswerResponse.OldLevel -> {}
      AnswerResponse.Wrong -> {
        e("Your answer $answer was wrong!")
        if ("too high" in body) {
          e("Too high!")
        } else if ("too low" in body) {
          e("Too low!")
        }
      }
      AnswerResponse.RateLimit -> {
        val time = Regex("You have (.*?) left to wait").find(body)?.groupValues[1]
        i("Rate Limited: $time")
      }
    }

    // Cache if successful
    if (result == AnswerResponse.Correct) {
      data.recordCorrectAnswer(answer)
      // } else if (result == AnswerResponse.OldLevel) {
      //   TODO JTW: for writing new files
      // data.recordCorrectAnswer(answer)
      // return AnswerResponse.Correct
    }

    return result
  }

  override fun close() {
    client.dispatcher.executorService.shutdown()
    client.connectionPool.evictAll()
  }
}

enum class AnswerResponse(val string: String) {
  Correct("That's the right answer!"),
  Wrong("That's not the right answer"),
  RateLimit("You gave an answer too recently"),
  OldLevel("You don't seem to be solving the right level"),
}
