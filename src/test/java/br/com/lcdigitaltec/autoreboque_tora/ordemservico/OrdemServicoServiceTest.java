package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import br.com.lcdigitaltec.autoreboque_tora.cliente.Cliente;
import br.com.lcdigitaltec.autoreboque_tora.cliente.ClienteRepository;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.LancamentoFinanceiroService;
import br.com.lcdigitaltec.autoreboque_tora.motorista.MotoristaRepository;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;

import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    /*
     * MOCKS:
     * Simulam as dependências utilizadas pelo OrdemServicoService.
     * Nenhum deles acessa PostgreSQL.
     */

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private MotoristaRepository motoristaRepository;

    @Mock
    private LancamentoFinanceiroService lancamentoFinanceiroService;

    @Mock
    private NumeroOrdemServicoService numeroOrdemServicoService;

    /*
     * Cria o service REAL e injeta os mocks acima.
     */
    @InjectMocks
    private OrdemServicoService ordemServicoService;

    /*
     * Cliente utilizado para construir uma OS válida.
     */
    @Mock
    private Cliente cliente;

    private OrdemServico ordemServico;

    /*
     * Executado antes de CADA teste.
     *
     * Assim cada teste começa com uma nova OS no status ABERTA.
     */
    @BeforeEach
    void setUp() {

        TipoServico tipoServico =
                TipoServico.values()[0];

        ordemServico = new OrdemServico(
                20260001L,
                cliente,
                null,
                null,
                tipoServico,
                "Ibirité - MG",
                "Belo Horizonte - MG",
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(250),
                BigDecimal.valueOf(80),
                "Teste automatizado",
                "ABC1D23",
                "Fiat",
                "Palio",
                "Prata",
                2014,
                BigDecimal.valueOf(120000),
                "Sem avarias"
        );
    }


    // ============================================================
    // BUSCA
    // ============================================================

    @Test
    void deveBuscarOrdemServicoExistente() {

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        OrdemServicoResponse response =
                ordemServicoService.buscarPorId(1L);

        assertNotNull(response);

        verify(ordemServicoRepository)
                .findById(1L);
    }


    @Test
    void deveLancarExcecaoQuandoOrdemNaoExistir() {

        when(ordemServicoRepository.findById(999L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception =
                assertThrows(
                        RecursoNaoEncontradoException.class,
                        () -> ordemServicoService.buscarPorId(999L)
                );

        assertEquals(
                "Ordem de serviço não encontrada",
                exception.getMessage()
        );

        verify(ordemServicoRepository)
                .findById(999L);
    }


    // ============================================================
    // INICIAR ATENDIMENTO
    // ============================================================

    @Test
    void deveIniciarOrdemAberta() {

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        ordemServicoService.iniciarAtendimento(1L);

        assertEquals(
                StatusOrdemServico.EM_ATENDIMENTO,
                ordemServico.getStatus()
        );

        verify(ordemServicoRepository)
                .findById(1L);
    }


    @Test
    void naoDeveIniciarOrdemCancelada() {

        ordemServico.cancelar();

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        RegraNegocioException exception =
                assertThrows(
                        RegraNegocioException.class,
                        () -> ordemServicoService.iniciarAtendimento(1L)
                );

        assertEquals(
                "Ordem de serviço cancelada não pode ser iniciada",
                exception.getMessage()
        );

        assertEquals(
                StatusOrdemServico.CANCELADA,
                ordemServico.getStatus()
        );
    }


    // ============================================================
    // CONCLUIR
    // ============================================================

    @Test
    void deveConcluirOrdemEmAtendimento() {

        ordemServico.iniciarAtendimento();

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        BigDecimal kmReal =
                BigDecimal.valueOf(25);

        ordemServicoService.concluir(
                1L,
                kmReal
        );

        assertEquals(
                StatusOrdemServico.CONCLUIDA,
                ordemServico.getStatus()
        );

        assertEquals(
                kmReal,
                ordemServico.getKmReal()
        );

        assertNotNull(
                ordemServico.getDataConclusao()
        );
    }


    @Test
    void naoDeveConcluirOrdemAberta() {

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        RegraNegocioException exception =
                assertThrows(
                        RegraNegocioException.class,
                        () -> ordemServicoService.concluir(
                                1L,
                                BigDecimal.valueOf(25)
                        )
                );

        assertEquals(
                "Somente ordem em atendimento pode ser concluída",
                exception.getMessage()
        );

        assertEquals(
                StatusOrdemServico.ABERTA,
                ordemServico.getStatus()
        );
    }


    // ============================================================
    // CANCELAR
    // ============================================================

    @Test
    void deveCancelarOrdemAberta() {

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        ordemServicoService.cancelar(1L);

        assertEquals(
                StatusOrdemServico.CANCELADA,
                ordemServico.getStatus()
        );
    }


    @Test
    void naoDeveCancelarOrdemConcluida() {

        ordemServico.iniciarAtendimento();
        ordemServico.concluir(
                BigDecimal.valueOf(25)
        );

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        RegraNegocioException exception =
                assertThrows(
                        RegraNegocioException.class,
                        () -> ordemServicoService.cancelar(1L)
                );

        assertEquals(
                "Ordem de serviço concluída não pode ser cancelada",
                exception.getMessage()
        );

        assertEquals(
                StatusOrdemServico.CONCLUIDA,
                ordemServico.getStatus()
        );
    }


    // ============================================================
    // FATURAR
    // ============================================================

    @Test
    void deveFaturarOrdemConcluida() {

        ordemServico.iniciarAtendimento();

        ordemServico.concluir(
                BigDecimal.valueOf(25)
        );

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        ordemServicoService.faturar(1L);

        assertEquals(
                StatusOrdemServico.FATURADA,
                ordemServico.getStatus()
        );

        verify(
                lancamentoFinanceiroService,
                times(1)
        ).registrarReceitaDaOrdemServico(
                ordemServico
        );
    }


    @Test
    void naoDeveFaturarOrdemAberta() {

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        RegraNegocioException exception =
                assertThrows(
                        RegraNegocioException.class,
                        () -> ordemServicoService.faturar(1L)
                );

        assertEquals(
                "Somente ordem de serviço concluída pode ser faturada",
                exception.getMessage()
        );

        verify(
                lancamentoFinanceiroService,
                never()
        ).registrarReceitaDaOrdemServico(
                any(OrdemServico.class)
        );
    }


    // ============================================================
    // DELETAR
    // ============================================================

    @Test
    void deveExcluirOrdemNaoFaturada() {

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        ordemServicoService.deletar(1L);

        verify(ordemServicoRepository)
                .delete(ordemServico);
    }


    @Test
    void naoDeveExcluirOrdemFaturada() {

        ordemServico.iniciarAtendimento();

        ordemServico.concluir(
                BigDecimal.valueOf(25)
        );

        ordemServico.faturar();

        when(ordemServicoRepository.findById(1L))
                .thenReturn(Optional.of(ordemServico));

        RegraNegocioException exception =
                assertThrows(
                        RegraNegocioException.class,
                        () -> ordemServicoService.deletar(1L)
                );

        assertEquals(
                "Ordem de serviço faturada não pode ser excluída",
                exception.getMessage()
        );

        verify(
                ordemServicoRepository,
                never()
        ).delete(any());
    }

    @Test
    void deveCadastrarOrdemServicoComSucesso() {

        // Arrange
        Long clienteId = 10L;
        Long numeroOs = 20260002L;

        TipoServico tipoServico = TipoServico.values()[0];

        OrdemServicoRequest request = mock(OrdemServicoRequest.class);

        when(request.clienteId()).thenReturn(clienteId);
        when(request.veiculoId()).thenReturn(null);
        when(request.motoristaId()).thenReturn(null);

        when(request.tipoServico()).thenReturn(tipoServico);
        when(request.origem()).thenReturn("Ibirité - MG");
        when(request.destino()).thenReturn("Belo Horizonte - MG");

        when(request.kmEstimado()).thenReturn(BigDecimal.valueOf(20));
        when(request.valorCobrado()).thenReturn(BigDecimal.valueOf(250));
        when(request.custoEstimado()).thenReturn(BigDecimal.valueOf(80));

        when(request.observacao()).thenReturn("Teste de cadastro");

        when(request.veiculoClientePlaca()).thenReturn("abc1d23");
        when(request.veiculoClienteMarca()).thenReturn("Fiat");
        when(request.veiculoClienteModelo()).thenReturn("Palio");
        when(request.veiculoClienteCor()).thenReturn("Prata");
        when(request.veiculoClienteAno()).thenReturn(2014);
        when(request.veiculoClienteKm())
                .thenReturn(BigDecimal.valueOf(120000));

        when(request.veiculoClienteObservacao())
                .thenReturn("Sem avarias");

        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(cliente));

        when(numeroOrdemServicoService.gerarProximoNumero())
                .thenReturn(numeroOs);

        when(ordemServicoRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // Act
        OrdemServicoResponse response =
                ordemServicoService.cadastrar(request);


        // Assert
        assertNotNull(response);

        ArgumentCaptor<OrdemServico> captor =
                ArgumentCaptor.forClass(OrdemServico.class);

        verify(ordemServicoRepository).save(captor.capture());

        OrdemServico ordemSalva = captor.getValue();

        assertNotNull(ordemSalva);

        assertEquals(numeroOs, ordemSalva.getNumeroOs());

        assertEquals(
                StatusOrdemServico.ABERTA,
                ordemSalva.getStatus()
        );

        assertEquals(
                "Ibirité - MG",
                ordemSalva.getOrigem()
        );

        assertEquals(
                "Belo Horizonte - MG",
                ordemSalva.getDestino()
        );

        assertEquals(
                BigDecimal.valueOf(250),
                ordemSalva.getValorCobrado()
        );

        assertEquals(
                "ABC1D23",
                ordemSalva.getVeiculoClientePlaca()
        );

        verify(clienteRepository, times(1))
                .findById(clienteId);

        verify(numeroOrdemServicoService, times(1))
                .gerarProximoNumero();

        verify(ordemServicoRepository, times(1))
                .save(any(OrdemServico.class));
    }
}