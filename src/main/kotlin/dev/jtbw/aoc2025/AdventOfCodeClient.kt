package dev.jtbw.aoc2025

import java.io.File
import kotlin.system.exitProcess
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
 * The session token is automatically read from `~/.aoc/session_token`. This file must exist and
 * contain your Advent of Code session token (obtained from the `session` cookie when logged into
 * adventofcode.com).
 *
 * ## Caching
 *
 * Puzzle inputs are cached locally in `~/.aoc/{year}/{day}.txt`. When [fetchInput] is called, the
 * cache is checked first. If the file exists, it is returned immediately without making an API
 * call. If not, the input is fetched from the API and cached for future use.
 *
 * Correct answers are cached in `~/.aoc/{year}/answer{part}.txt`. When [submitAnswer] is called, if
 * the cached answer matches the provided answer, true is returned immediately. If the submission
 * succeeds (HTTP 200), the answer is cached for future calls.
 *
 * @throws IllegalStateException if `~/.aoc/session_token` does not exist
 */
class AdventOfCodeClient : AutoCloseable {
  private val service: AdventOfCodeService
  private val sessionToken: String
  private val cacheDir: File
  private val client = OkHttpClient.Builder().build()

  init {
    val homeDir = System.getProperty("user.home")
    val aocDir = File(homeDir, ".aoc")
    val sessionFile = File(aocDir, "session_token")

    sessionToken =
      if (sessionFile.exists()) {
        sessionFile.readText().trim()
      } else {
        throw IllegalStateException(
          "Session token not found. Please create ${sessionFile.absolutePath}"
        )
      }

    cacheDir = aocDir

    val retrofit =
      Retrofit.Builder()
        .baseUrl("https://adventofcode.com/")
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    service = retrofit.create(AdventOfCodeService::class.java)
  }

  /**
   * Fetches the puzzle input for the specified year and day.
   *
   * This function first checks the local cache at `~/.aoc/{year}/{day}.txt`. If the file exists,
   * its contents are returned immediately. Otherwise, the input is fetched from
   * `https://adventofcode.com/{year}/day/{day}/input` and cached locally.
   *
   * @param year the year of the puzzle (e.g., 2025)
   * @param day the day of the puzzle (1-25)
   * @return the puzzle input as a string
   */
  suspend fun fetchInput(year: Int, day: Int): String {
    val yearDir = File(cacheDir, year.toString())
    val cacheFile = File(yearDir, "$day.txt")

    // Check cache first
    if (cacheFile.exists()) {
      return cacheFile.readText()
    }

    // Fetch from API
    val input = service.getInput(year, day, "session=$sessionToken").trim()

    // Cache the result
    yearDir.mkdirs()
    cacheFile.writeText(input)

    return input
  }

  /**
   * Submits an answer for a puzzle.
   *
   * This function first checks the local cache at `~/.aoc/{year}/answer{part}.txt`. If the cached
   * answer matches the provided answer, it returns true immediately. Otherwise, it posts the answer
   * to `https://adventofcode.com/{year}/day/{day}/answer`. If the response status code is 200, the
   * answer is cached and true is returned. Otherwise, false is returned.
   *
   * @param year the year of the puzzle (e.g., 2025)
   * @param day the day of the puzzle (1-25)
   * @param part the part number (1 or 2)
   * @param answer your answer as a string
   * @return true if the answer is correct (or was previously correct), false otherwise
   */
  suspend fun submitAnswer(year: Int, day: Int, part: Int, answer: String): AnswerResponse {
    val yearDir = File(cacheDir, year.toString())
    val answerFile = File(yearDir, "answer${day}_$part.txt")

    // Check cache first
    if (answerFile.exists()) {
      val cachedAnswer = answerFile.readText().trim()
      if (cachedAnswer == answer) {
        return AnswerResponse.Correct
      } else {
        return AnswerResponse.Wrong
      }
    }

    if (!submit) {
      i("❓ Day $day part $part: answer $answer not submitted")
      exitProcess(0)
    }

    // Submit to API
    val response = service.submitAnswer(year, day, part, answer, "session=$sessionToken")
    val body = response.body() ?: ""

    val result =
      AnswerResponse.entries.firstOrNull() { it.string in body }
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
      }
      AnswerResponse.RateLimit -> {
        val time = Regex("You have (.*?) left to wait").find(body)?.groupValues[1]
        i("Rate Limited: $time")
      }
    }

    // Cache if successful
    if (result == AnswerResponse.Correct) {
      yearDir.mkdirs()
      answerFile.writeText(answer)
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
