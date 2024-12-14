package com.tendaysonly.ringly.service

import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.util.TokenUtils
import com.tendaysonly.ringly.util.variable
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

/**
 * @author oognuyh
 */
@Component
class MailService(
    private val sender: JavaMailSender,
    private val templateEngine: TemplateEngine,
    private val tokenUtils: TokenUtils
) {

    data class Recipient(
        val email: String,
        val name: String
    )


    /**
     * Sends the invitation to the recipient.
     *
     */
    fun sendInvitation(recipient: Recipient, gathering: Gathering) =
        send(
            recipient = recipient,
            title = "지금 Ringly로 모임에 참가하세요!",
            content = templateEngine.process(
                "invitation", Context()
                    .variable("name", gathering.name)
                    .variable("dressCode", gathering.dressCode)
                    .variable("location", gathering.location)
                    .variable(
                        "meetAt", if (gathering.meetAt != null) {

                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                            formatter.format(gathering.meetAt!!.withZoneSameInstant(ZoneId.of("Asia/Seoul")))
                        } else {
                            null
                        }
                    )
                    .variable("additionalInfo", gathering.additionalInfo)
                    .variable(
                        "link",
                        "https://ringly.oognuyh.com/invite-room/${gathering.gatheringId}/participants?token=${
                            tokenUtils.generate(
                                recipient.email,
                                mapOf(
                                    "name" to recipient.name,
                                )
                            )
                        }"
                    )
            )
        )

    /**
     * Sends a mail to the recipient.
     */
    fun send(
        recipient: Recipient,
        title: String,
        content: String,
        isHtml: Boolean = true
    ): CompletableFuture<Void> = CompletableFuture.runAsync {

        try {
            println("Try to send a mail to ${recipient}.")

            val message = this.sender.createMimeMessage()

            MimeMessageHelper(message, true, "utf-8").apply {

                setTo(recipient.email)
                setSubject(title)
                setText(content, isHtml)
            }

            this.sender.send(message)

            println("Successfully sent to ${recipient.email}.")
        } catch (e: Exception) {

            println("Failed to send email to ${recipient.email}: ${e.message}")
        }
    }
}