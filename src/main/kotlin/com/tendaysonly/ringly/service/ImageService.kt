package com.tendaysonly.ringly.service

import com.amazonaws.services.s3.AmazonS3
import jakarta.validation.constraints.NotNull
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
class ImageService(
    private val s3Client: AmazonS3
) {

    /**
     * Uploads a image to the defined object storage.
     */
    fun uploadImage(@NotNull image: String): String {

        return "https://img.freepik.com/free-photo/cat-wearing-santa-hat-adorable-looks-stylish_1268-27861.jpg?t=st=1733765407~exp=1733769007~hmac=3abd2105da8569c5de771108fccda8a6f7b06aa0511aba76d34609aac3e9ed69&w=2000"
    }
}