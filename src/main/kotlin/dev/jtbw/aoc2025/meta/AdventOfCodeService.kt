package dev.jtbw.aoc2025.meta

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AdventOfCodeService {
  @GET("{year}/day/{day}/input")
  suspend fun getInput(
      @Path("year") year: Int,
      @Path("day") day: Int,
      @Header("Cookie") sessionCookie: String,
  ): String

  @POST("{year}/day/{day}/answer")
  @FormUrlEncoded
  suspend fun submitAnswer(
      @Path("year") year: Int,
      @Path("day") day: Int,
      @Field("level") level: Int,
      @Field("answer") answer: String,
      @Header("Cookie") sessionCookie: String,
  ): Response<String>
}