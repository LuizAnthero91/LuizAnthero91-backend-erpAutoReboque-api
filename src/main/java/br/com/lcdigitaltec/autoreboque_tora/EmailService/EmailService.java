package br.com.lcdigitaltec.autoreboque_tora.email;

import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.Usuario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final String frontendUrl;
    private final String remetente;

    public EmailService(
            JavaMailSender mailSender,

            @Value("${app.frontend-url}")
            String frontendUrl,

            @Value("${app.mail.from}")
            String remetente
    ) {
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
        this.remetente = remetente;
    }

    public void enviarRecuperacaoSenha(
            Usuario usuario,
            String token
    ) {

        String link = UriComponentsBuilder
                .fromUriString(frontendUrl)
                .path("/redefinir-senha")
                .queryParam("token", token)
                .build()
                .encode()
                .toUriString();

        try {

            MimeMessage mensagem =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mensagem,
                            false,
                            "UTF-8"
                    );

            helper.setFrom(remetente);

            helper.setTo(
                    usuario.getEmail()
            );

            helper.setSubject(
                    "Recuperação de senha - Auto Reboque Torá"
            );

            String html = """
                    <html>
                    <body>
                    
                        <h2>Recuperação de senha</h2>
                    
                        <p>Olá, %s.</p>
                    
                        <p>
                            Recebemos uma solicitação para redefinir
                            a senha da sua conta.
                        </p>
                    
                        <p>
                            <a href="%s">
                                Redefinir minha senha
                            </a>
                        </p>
                    
                        <p>
                            Este link é válido por 30 minutos.
                        </p>
                    
                        <p>
                            Se você não solicitou a alteração,
                            ignore este e-mail.
                        </p>
                    
                    </body>
                    </html>
                    """.formatted(
                    usuario.getNome(),
                    link
            );

            helper.setText(
                    html,
                    true
            );

            mailSender.send(mensagem);

        } catch (MessagingException e) {

            throw new IllegalStateException(
                    "Não foi possível montar o e-mail de recuperação.",
                    e
            );
        }
    }
}