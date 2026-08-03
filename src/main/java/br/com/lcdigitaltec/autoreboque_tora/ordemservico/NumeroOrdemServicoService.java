package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
public class NumeroOrdemServicoService {

    private static final int LIMITE_ANUAL = 9999;

    private final JdbcTemplate jdbcTemplate;

    public NumeroOrdemServicoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Long gerarProximoNumero() {
        int anoAtual = Year.now().getValue();

        Integer sequencial = jdbcTemplate.queryForObject(
                """
                INSERT INTO sequencias_ordem_servico (
                    ano,
                    ultimo_sequencial
                )
                VALUES (?, 1)
                ON CONFLICT (ano)
                DO UPDATE
                SET ultimo_sequencial =
                    sequencias_ordem_servico.ultimo_sequencial + 1
                RETURNING ultimo_sequencial
                """,
                Integer.class,
                anoAtual
        );

        if (sequencial == null) {
            throw new RegraNegocioException(
                    "Não foi possível gerar o número da ordem de serviço."
            );
        }

        if (sequencial > LIMITE_ANUAL) {
            throw new RegraNegocioException(
                    "O limite anual de 9.999 ordens de serviço foi atingido."
            );
        }

        return Long.parseLong(
                "%d%04d".formatted(anoAtual, sequencial)
        );
    }
}