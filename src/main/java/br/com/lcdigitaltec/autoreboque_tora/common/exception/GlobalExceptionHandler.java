package br.com.lcdigitaltec.autoreboque_tora.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocio(
            RegraNegocioException exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.BAD_REQUEST,
                "Regra de negócio violada",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarErroValidacao(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ErroCampoResponse> campos = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> new ErroCampoResponse(
                        erro.getField(),
                        erro.getDefaultMessage()
                ))
                .toList();

        ErroResponse erro = criarErro(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Existem campos inválidos na requisição",
                request.getRequestURI(),
                campos
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> tratarConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.BAD_REQUEST,
                "JSON inválido",
                "Verifique o corpo da requisição. Pode existir campo inválido, enum incorreto ou formato de data errado.",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarViolacaoBanco(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.CONFLICT,
                "Violação de integridade",
                "Não foi possível salvar os dados. Pode existir informação duplicada ou vínculo com outro registro.",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> tratarAcessoNegado(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para acessar este recurso.",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInterno(
            Exception exception,
            HttpServletRequest request
    ) {
        ErroResponse erro = criarErro(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente ou contate o suporte.",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    private ErroResponse criarErro(
            HttpStatus status,
            String erro,
            String mensagem,
            String path,
            List<ErroCampoResponse> campos
    ) {
        return new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                erro,
                mensagem,
                path,
                campos
        );
    }
}