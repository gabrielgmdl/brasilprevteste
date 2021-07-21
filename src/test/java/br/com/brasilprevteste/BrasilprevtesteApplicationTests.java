package br.com.brasilprevteste;

import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.repository.Filter.PedidoFilter;
import br.com.brasilprevteste.repository.pedido.PedidoRepository;
import br.com.brasilprevteste.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DisplayName("Aplicação Brasil Prev Testes")
@SpringBootTest(classes = BrasilprevtesteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrasilprevtesteApplicationTests {

	@Nested
	@DisplayName("Pedido")
	class PedidoTestes {
		@DisplayName("Testando End Point, token valido.")
		@Nested
		class TokenValido {
			@Autowired
			private MockMvc mockMvc;

			@MockBean
			private PedidoService service;

			@Mock
			private Page<Pedido> page;

			private final MediaType contentType = new MediaType("application", "json");
			private final Pageable pageable = PageRequest.of(0, 10);
			private final PedidoFilter filter = new PedidoFilter(null, null);
			private final PedidoFilter filterNome = new PedidoFilter(20.2, null);
			private final PedidoFilter filterDescricao = new PedidoFilter(null, "descrição");
			private final Pedido pedido = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "descrição");
			private ObjectMapper mapper;

			@BeforeEach
			void init() {
				mapper = new ObjectMapper();
			}

			@Test
			@DisplayName("Listar Pedido, retornar a Pedido e status 200")
			public void listarPedidos() throws Exception {
				when(service.filtrar(filter, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
				verify(service, times(1))
						.filtrar(filter, pageable);
			}

			@Test
			@DisplayName("Listar Pedido usando filtro pelo nome, retornar a Pedido e status 200")
			public void listarPedidosNome() throws Exception {
				when(service.filtrar(filterNome, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos?nome=nome")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
				verify(service, times(1)).filtrar(filterNome, pageable);
			}

			@Test
			@DisplayName("Listar Pedido usando filtro pela descrição, retornar a Pedido e status 200")
			public void listarPedidosDescricao() throws Exception {
				when(service.filtrar(filterDescricao, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos?descricao=descrição")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
				verify(service, times(1))
						.filtrar(filterDescricao, pageable);
			}

			@Test
			@DisplayName("Buscar Pedido usando o id, retornar a Pedido e status 200 sucesso")
			public void buscarPedido() throws Exception {
				when(service.detalhar(3L)).thenReturn(pedido);
				mockMvc.perform(get("/pedidos/{id}", 3L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isOk())
						.andDo(print())
						.andExpect(jsonPath("$.descricao").value("descrição"));
				verify(service, times(1))
						.detalhar(3L);
			}

			@Test
			@DisplayName("Deletar Pedido, retornar a Pedido e status 200")
			public void deletarPedido() throws Exception {
				mockMvc.perform(delete("/pedidos/{id}", 3L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
				verify(service, times(1))
						.deletar(3L);
			}

			@Test
			@DisplayName("Criar Pedido, retornar a Pedido e status 201")
			public void criarPedido() throws Exception {
				when(service.salvar(pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isCreated())
						.andDo(print())
						.andExpect(jsonPath("$.nome").value("nome"))
						.andExpect(jsonPath("$.descricao").value("descrição"));
				verify(service, times(1))
						.salvar(Mockito.any(Pedido.class));
			}

			@Test
			@DisplayName("Atualizar Pedido, retornar a Pedido e status 201")
			public void atualizarPedido() throws Exception {
				when(service.atualizar(1L, pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(put("/pedidos/{id}", 1L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isCreated())
						.andDo(print())
						.andExpect(jsonPath("$.nome").value("nome"))
						.andExpect(jsonPath("$.descricao").value("descrição"));
				verify(service, times(1))
						.atualizar(1L, pedido);
			}
		}

		@Nested
		@DisplayName("Testando End Point, token invalido.")
		class TokenInvalido {
			@Autowired
			private MockMvc mockMvc;

			@MockBean
			private PedidoService service;

			@Mock
			private Page<Pedido> page;

			private final MediaType contentType = new MediaType("application", "json");
			private final Pageable pageable = PageRequest.of(0, 10);
			private final PedidoFilter filter = new PedidoFilter(null, null);
			private final Pedido pedido = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "teste");
			private ObjectMapper mapper;

			@BeforeEach
			void init() {
				mapper = new ObjectMapper();
			}

			@Test
			@DisplayName("Listar Pedidos com usuario e senha incorretos, retornar status 401")
			public void listamosPedidosTokenIncorreto() throws Exception {
				when(service.filtrar(filter, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("a", "a", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("invalid_token"));
				verify(service, times(0))
						.filtrar(filter, pageable);
			}

			@Test
			@DisplayName("Buscar Pedido usando usuario e senha incorretos, retornar status 401")
			public void buscarPedidosTokenIncorreto() throws Exception {
				when(service.detalhar(3L)).thenReturn(pedido);
				mockMvc.perform(get("/pedidos/{id}", 3L)
						.header("Authorization", "Bearer " + Util.getAccessToken("a", "a", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("invalid_token"));
				verify(service, times(0))
						.detalhar(3L);
			}

			@Test
			@DisplayName("Deletar Pedido com usuario e senha incorretos, retornar status 401")
			public void deletarPedidosTokenIncorreto() throws Exception {
				when(service.deletar(3L)).thenReturn(pedido);
				mockMvc.perform(delete("/pedidos/1")
						.header("Authorization", "Bearer " + Util.getAccessToken("a", "a", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("invalid_token"));
				verify(service, times(0))
						.deletar(1L);
			}

			@Test
			@DisplayName("Criar Pedido com usuario e senha incorretos, retornar status 401")
			public void criarPedidosTokenIncorreto() throws Exception {
				when(service.salvar(pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("a", "a", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("invalid_token"));
				verify(service, times(0))
						.atualizar(1L, pedido);
			}

			@Test
			@DisplayName("Atualizar Pedido com usuario e senha incorretos, retornar status 401")
			public void atualizarPedidosTokenIncorreto() throws Exception {
				when(service.atualizar(1L, pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(put("/pedidos/1")
						.header("Authorization", "Bearer " + Util.getAccessToken("a", "a", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("invalid_token"));
				verify(service, times(0))
						.atualizar(1L, pedido);
			}

			@Test
			@DisplayName("Listar Pedidos sem token, retornar status 401")
			public void listamosSemToken() throws Exception {
				when(service.filtrar(filter, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos"))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("unauthorized"));
				verify(service, times(0))
						.filtrar(filter, pageable);
			}

			@Test
			@DisplayName("Buscar Pedido sem token, retornar status 401")
			public void buscarSemToken() throws Exception {
				when(service.detalhar(3L)).thenReturn(pedido);
				mockMvc.perform(get("/pedidos/{id}", 3L))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("unauthorized"));
				verify(service, times(0))
						.detalhar(3L);
			}

			@Test
			@DisplayName("Deletar Pedido sem token, retornar status 401")
			public void deletarSemToken() throws Exception {
				when(service.deletar(3L)).thenReturn(pedido);
				mockMvc.perform(delete("/pedidos/1"))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("unauthorized"));
				verify(service, times(0))
						.deletar(1L);
			}

			@Test
			@DisplayName("Criar Pedido sem token, retornar status 401")
			public void criarSemToken() throws Exception {
				when(service.salvar(pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(post("/pedidos")
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("unauthorized"));
				verify(service, times(0))
						.salvar(pedido);
			}

			@Test
			@DisplayName("Atualizar Pedido sem token, retornar status 401")
			public void atualizarSemToken() throws Exception {
				when(service.atualizar(1L, pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(put("/pedidos/1")
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isUnauthorized())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("unauthorized"));
				verify(service, times(0))
						.atualizar(1L, pedido);
			}
		}

		@Nested
		@DisplayName("Testando End Point, acesso sem permissão.")
		class SemPermissao {
			@Autowired
			private MockMvc mockMvc;

			@MockBean
			private PedidoService service;

			@Mock
			private Page<Pedido> page;

			private final MediaType contentType = new MediaType("application", "json");
			private final Pageable pageable = PageRequest.of(0, 10);
			private final PedidoFilter filter = new PedidoFilter(null, null);
			private final PedidoFilter filterNome = new PedidoFilter(19.2, null);
			private final PedidoFilter filterDescricao = new PedidoFilter(null, "descrição");
			private final Pedido pedido = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "teste");
			private ObjectMapper mapper;

			@BeforeEach
			void init() {
				mapper = new ObjectMapper();
			}

			@Test
			@DisplayName("Listar Pedido sem permissão de acesso, retornar a Pedido o status 403")
			public void permissaoListarPedidos() throws Exception {
				when(service.filtrar(filter, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.filtrar(filter, pageable);
			}

			@Test
			@DisplayName("Listar Pedido usando filtro pelo nome sem permissão de acesso, retornar o status 403")
			public void permissaoListarPedidosNome() throws Exception {
				when(service.filtrar(filterNome, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos?nome=nome")
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.filtrar(filterNome, pageable);
			}

			@Test
			@DisplayName("Listar Pedido usando filtro pela descrição sem permissão de acesso, retornar o status 403")
			public void permissaoListarPedidosDescricao() throws Exception {
				when(service.filtrar(filterDescricao, pageable)).thenReturn(page);
				mockMvc.perform(get("/pedidos?descricao=descrição")
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.filtrar(filterDescricao, pageable);
			}

			@Test
			@DisplayName("Buscar Pedido usando o id sem permissão de acesso, retornar o status 403")
			public void permissaoBuscarPedido() throws Exception {
				when(service.detalhar(3L)).thenReturn(pedido);
				mockMvc.perform(get("/pedidos/{id}", 3L)
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.detalhar(3L);
			}

			@Test
			@DisplayName("Deletar Pedido sem permissão de acesso, retornar a Pedido e status 403")
			public void permissaoDeletarPedido() throws Exception {
				mockMvc.perform(delete("/pedidos/{id}", 3L)
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.deletar(3L);
			}

			@Test
			@DisplayName("Criar Pedido sem permissão de acesso, retornar o status 403")
			public void permissaoCriarPedido() throws Exception {
				when(service.salvar(pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.salvar(Mockito.any(Pedido.class));
			}

			@Test
			@DisplayName("Atualizar Pedido sem permissão de acesso, retornar o status 403")
			public void permissaoAtualizarPedido() throws Exception {
				when(service.atualizar(1L, pedido)).thenReturn(pedido);
				String jsonString = mapper.writeValueAsString(pedido);
				mockMvc.perform(put("/pedidos/{id}", 1L)
						.header("Authorization", "Bearer " + Util.getAccessToken("user", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(contentType))
						.andExpect(status().isForbidden())
						.andDo(print())
						.andExpect(jsonPath("$.error").value("access_denied"));
				verify(service, times(0))
						.atualizar(1L, pedido);
			}
		}

		@Nested
		@DisplayName("Testando End Point, validações das entidades.")
		class ValidacoesEntidade {
			@Autowired
			private MockMvc mockMvc;

			@MockBean
			private PedidoService service;

			private final Pedido descricaoNull = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), null);
			private final Pedido descricaoVazia = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "");
			private final Pedido descricaoMaior50 = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque sit amet volutpat sapien, in feugiat leo. In non dui risus. In sollicitudin ligula nec dignissim ultricies. Suspendisse potenti. Fusce a purus massa. Mauris ultrices, nisi at vehicula cursus, est nulla commodo nulla, id tempor massa velit eu nunc. Aliquam quis hello.");
			private final Pedido descricaoMenor4 = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "Lo.");
			private ObjectMapper mapper;

			@BeforeEach
			void init() {
				mapper = new ObjectMapper();
			}

			@Test
			@DisplayName("Criar Pedido informando um nome vazio, retornar mensagem de erro e status 400.")
			public void criarNomeVazio() throws Exception {
				when(service.salvar(Mockito.any(Pedido.class))).thenReturn(descricaoVazia);
				String jsonString = mapper.writeValueAsString(descricaoVazia);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome deve ter entre 4 e 50 caracteres."));
				verify(service, times(0))
						.salvar(descricaoVazia);
			}

			@Test
			@DisplayName("Atualizar Pedido informando um nome vazio, retornar mensagem de erro e status 400.")
			public void atualizarNomeVazio() throws Exception {
				when(service.atualizar(1L, descricaoVazia)).thenReturn(descricaoVazia);
				String jsonString = mapper.writeValueAsString(descricaoVazia);
				mockMvc.perform(put("/pedidos/{id}", 1L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome deve ter entre 4 e 50 caracteres."));
				verify(service, times(0))
						.atualizar(1L, descricaoVazia);
			}

			@Test
			@DisplayName("Criar Pedido informando nome e descricao, retornar as informações enviadas e Status 201")
			public void criarNomeNull() throws Exception {
				when(service.salvar(Mockito.any(Pedido.class))).thenReturn(descricaoNull);
				String jsonString = mapper.writeValueAsString(descricaoNull);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome não pode ser null."));
				verify(service, times(0))
						.salvar(descricaoNull);
			}

			@Test
			@DisplayName("Atualizar Pedido sem informar uma descricao, retornar as informações e status 201")
			public void atualizarNomeNull() throws Exception {
				when(service.atualizar(1L, descricaoNull)).thenReturn(descricaoNull);
				String jsonString = mapper.writeValueAsString(descricaoNull);
				mockMvc.perform(put("/pedidos/{id}", 1L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome não pode ser null."));
				verify(service, times(0))
						.atualizar(1L, descricaoNull);
			}

			@Test
			@DisplayName("Criar Pedido informando uma descriçao acima de 50 caracteres, retornar mensagem de erro e status 400.")
			public void criarDescricaoAcima50Caracteres() throws Exception {
				when(service.salvar(Mockito.any(Pedido.class))).thenReturn(descricaoMaior50);
				String jsonString = mapper.writeValueAsString(descricaoMaior50);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome deve ter entre 4 e 50 caracteres."));
				verify(service, times(0))
						.salvar(descricaoMaior50);
			}

			@Test
			@DisplayName("Atualizar Pedido informando uma descriçao acima de 50 caracteres, retornar mensagem de erro e status 400.")
			public void atualizarDescricaoAcima50Caracteres() throws Exception {
				when(service.atualizar(1L, descricaoMaior50)).thenReturn(descricaoMaior50);
				String jsonString = mapper.writeValueAsString(descricaoMaior50);
				mockMvc.perform(put("/pedidos/{id}", 1L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome deve ter entre 4 e 50 caracteres."));
				verify(service, times(0))
						.atualizar(1L, descricaoMaior50);
			}

			@Test
			@DisplayName("Criar Pedido informando uma descriçao abaixo de 4 caracteres, retornar mensagem de erro e status 400.")
			public void criarDescricaoAbaixo4Caracteres() throws Exception {
				when(service.salvar(Mockito.any(Pedido.class))).thenReturn(descricaoMenor4);
				String jsonString = mapper.writeValueAsString(descricaoMenor4);
				mockMvc.perform(post("/pedidos")
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome deve ter entre 4 e 50 caracteres."));
				verify(service, times(0))
						.salvar(descricaoMenor4);
			}
			@Test
			@DisplayName("Atualizar Pedido informando um descriçao abaixo 4 caracteres, retornar mensagem de erro e status 400.")
			public void atualizarDescricaoAbaixo4Caracteres() throws Exception {
				when(service.atualizar(1L, descricaoMenor4)).thenReturn(descricaoMenor4);
				String jsonString = mapper.writeValueAsString(descricaoMenor4);
				mockMvc.perform(put("/pedidos/{id}", 1L)
						.header("Authorization", "Bearer " + Util.getAccessToken("admin", "123321", mockMvc))
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest())
						.andDo(print())
						.andExpect(jsonPath("$.field").value("nome"))
						.andExpect(jsonPath("$.fieldMessage").value("O nome deve ter entre 4 e 50 caracteres."));
				verify(service, times(0))
						.atualizar(1L, descricaoMenor4);
			}
		}

		@Nested
		@DisplayName("Testando o repositorio.")
		class Repository {
			@Autowired
			private PedidoRepository repository;

			@AfterEach
			void setUp() {
				repository.deleteAll();
			}

			@Test
			@DisplayName("Criar Pedido e persistir os dados, retornar: id, nome e descrição")
			public void criar() {
				Pedido pedido = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "Lorem");
				pedido = this.repository.save(pedido);
				Pedido pedidoNovo = repository.findById(pedido.getId()).orElse(null);
				assert pedidoNovo != null;
				assertThat(pedidoNovo.getId()).isNotNull();
				assertEquals("ValorTotal", pedidoNovo.getValorTotal());
				assertEquals("Descricao", pedidoNovo.getDescricao());
			}

			@Test
			@DisplayName("Deletar uma Pedido")
			public void deletar() {
				Pedido pedido = new Pedido(1L, new Cliente(), null, 2.50, LocalDateTime.now(), "Lorem");
				this.repository.save(pedido);
				this.repository.delete(pedido);
				assertThat(repository.findById(pedido.getId())).isEmpty();
			}

			@Test
			@DisplayName("Atualizar uma Pedido e persistir os dados, retorno: id, nome e descrição")
			public void atualizar() {
				Pedido pedido = new Pedido(3L, new Cliente(), null, 2.50, LocalDateTime.now(), "Lorem");
				pedido = this.repository.save(pedido);
				pedido = new Pedido(pedido.getId(), new Cliente(), null, 2.50, LocalDateTime.now(), "Descrição dois");
				pedido = this.repository.save(pedido);
				assertThat(pedido.getDescricao()).isEqualTo("Descrição dois");
			}

			@Test
			@DisplayName("Buscar Pedido por id, retorno: id, nome e descrição")
			public void buscarPorId() {
				Pedido pedido = new Pedido(9L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem");
				pedido = this.repository.save(pedido);
				assertThat(pedido.getId()).isNotNull();
				assertThat(pedido.getDescricao()).isEqualTo("Lorem");
			}

			@Test
			@DisplayName("Salva todas as entidades fornecidas.")
			public void salvarTodosEntidades() {
				List<Pedido> pedidoSalvos = this.repository.saveAll(new ArrayList<>(Arrays.asList(new Pedido(9L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(19L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2"))));
				assertThat(pedidoSalvos.get(0).getId()).isNotNull();
				assertThat(pedidoSalvos.get(0).getDescricao()).isEqualTo("Lorem");
				assertThat(pedidoSalvos.get(1).getId()).isNotNull();
				assertThat(pedidoSalvos.get(1).getDescricao()).isEqualTo("Lorem2");
			}

			@Test
			@DisplayName("Exclui uma determinada entidade.")
			public void deletarEntidade() {
				Pedido pedido = new Pedido(9L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem");
				this.repository.delete(pedido);
				assertThat(repository.findById(pedido.getId())).isEmpty();
			}

			@Test
			@DisplayName("Exclui as entidades fornecidas.")
			public void deletarTodosPorEntidades() {
				List<Pedido> pedidos = Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(2L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2"));
				this.repository.saveAll(pedidos);
				this.repository.deleteAll(pedidos);
				assertThat(repository.findById(1L)).isEmpty();
				assertThat(repository.findById(2L)).isEmpty();
			}

			@Test
			@DisplayName("Exclui todas as entidades gerenciadas pelo repositório.")
			public void deletarTudo() {
				List<Pedido> pedidos = Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(2L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2"));
				this.repository.saveAll(pedidos);
				this.repository.deleteAll();
				assertThat(repository.findById(1L)).isEmpty();
				assertThat(repository.findById(2L)).isEmpty();
			}

			@Test
			@DisplayName("Retorna todas as instâncias do tipo.")
			public void buscarTodosEntidades() {
				List<Pedido> pedidos = Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(2L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2"));
				this.repository.saveAll(pedidos);
				List<Pedido> pedidoBusca = this.repository.findAll();
				assertThat(pedidoBusca.get(0).getId()).isNotNull();
				assertThat(pedidoBusca.get(0).getDescricao()).isEqualTo("Lorem");
				assertThat(pedidoBusca.get(1).getId()).isNotNull();
				assertThat(pedidoBusca.get(1).getDescricao()).isEqualTo("Lorem1");
			}

			@Test
			@DisplayName("Seleciona varias entidades fornecendo os IDs existe.")
			public void buscarIds() {
				List<Pedido> pedidosSalvas = this.repository.saveAll(Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(2L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2")));
				List<Pedido> pedidos = this.repository.findAllById(Arrays.asList(pedidosSalvas.get(0).getId(), pedidosSalvas.get(1).getId()));
				assertThat(pedidos.get(0).getId()).isNotNull();
				assertThat(pedidos.get(0).getDescricao()).isEqualTo("Lorem");
				assertThat(pedidos.get(1).getId()).isNotNull();
				assertThat(pedidos.get(1).getDescricao()).isEqualTo("Lorem2");
			}

			@Test
			@DisplayName("Verificar se exite o ID fornecido.")
			public void verificarSeExistePorId() {
				Pedido pedido = this.repository.save(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"));
				assertTrue(this.repository.existsById(pedido.getId()));
			}
		}

		@Nested
		@DisplayName("Testando as validações da service.")
		class Service {

			@Autowired
			private PedidoService service;

			@Autowired
			private PedidoRepository repository;

			@AfterEach
			void setUp() {
				repository.deleteAll();
			}

			@Test
			@DisplayName("Criar Pedido com descrição, persistir os dados.")
			public void Criar() {
				Pedido pedido = this.service.salvar(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"));
				assertThat(pedido.getId()).isNotNull();
				assertEquals("Lorem", pedido.getDescricao());
				assertEquals(2.51, pedido.getValorTotal());
			}

			@Test
			@DisplayName("Criar Pedido com descrição, persistir os dados.")
			public void CriarNomeNull() {
				Pedido pedido = this.service.salvar(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), null));
				assertThat(pedido.getId()).isNotNull();
				assertEquals(null, pedido.getDescricao());
				assertEquals("", pedido.getDescricao());
			}

			@Test
			@DisplayName("Deletar verificar se foi removido.")
			public void deletar() {
				Pedido pedido = this.service.salvar(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"));
				service.deletar(pedido.getId());
				assertThat(repository.findById(pedido.getId())).isEmpty();
			}

			@Test
			@DisplayName("Detalhar retornar uma entidade selecionada por id de pedido.")
			public void detalhar() {
				Pedido pedidoSalva = this.repository.save(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"));
				Pedido pedido = service.deletar(pedidoSalva.getId());
				assertThat(pedido.getId()).isNotNull();
				assertEquals(null, pedido.getProdutos());
				assertEquals("Lorem", pedido.getDescricao());
			}

			@Test
			@DisplayName("Filtrar itens selecionados ")
			public void filtrar() {
				repository.saveAll(Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(2L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2")));
				PedidoFilter filtro = new PedidoFilter(2.51, "Lorem");
				Pageable pageable = PageRequest.of(0, 10);
				Page<Pedido> pedidoPage = service.filtrar(filtro, pageable);
				assertEquals(2.51, pedidoPage.getContent().get(0).getDescricao());
				assertEquals("Lorem", pedidoPage.getContent().get(0).getDescricao());
			}

			@Test
			@DisplayName("Filtrar itens selecionados filtrar por valor total e descriçao.")
			public void filtrarPorValorTotalEDescricao() {
				repository.saveAll(Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"), new Pedido(2L, new Cliente(), null, 2.21, LocalDateTime.now(), "Lorem2")));
				PedidoFilter filtro = new PedidoFilter(2.51, "Lorem");
				Pageable pageable = PageRequest.of(0, 2);
				Page<Pedido> pedidoPage = service.filtrar(filtro, pageable);
				assertEquals(2.51, pedidoPage.getContent().get(0).getValorTotal());
				assertEquals("Lorem", pedidoPage.getContent().get(0).getDescricao());
				assertEquals(2.21, pedidoPage.getContent().get(1).getValorTotal());
				assertEquals("Lorem1", pedidoPage.getContent().get(1).getDescricao());
				assertThat(pedidoPage.getSize()).isEqualTo(2);
			}

			@Test
			@DisplayName("Filtrar itens selecionados filtrar por descrição.")
			public void filtrarPorDescricao() {
				repository.saveAll(Arrays.asList(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem")));
				PedidoFilter filtro = new PedidoFilter(2.51, "Lorem1");
				Pageable pageable = PageRequest.of(0, 10);
				Page<Pedido> pedidoPage = service.filtrar(filtro, pageable);
				assertEquals(2.51, pedidoPage.getContent().get(0).getValorTotal());
				assertEquals("Lorem", pedidoPage.getContent().get(0).getDescricao());
			}

			@Test
			@DisplayName("Atualizar uma entidade de pedido por id.")
			public void atualizar() {
				Pedido pedidoSalva = this.service.salvar(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"));
				Pedido pedido = this.service.atualizar(pedidoSalva.getId(), new Pedido(null, new Cliente(), null, 2.51, LocalDateTime.now(), "Lorem"));
				assertThat(pedido.getId()).isNotNull();
				assertEquals(2.51, pedido.getValorTotal());
				assertEquals("Descrição2", pedido.getDescricao());
			}

			@Test
			@DisplayName("Criar Pedido com descrição, persistir os dados.")
			public void CriarComDescricaoNull() {
				Exception exception = assertThrows(TransactionSystemException.class, () -> service.salvar(new Pedido(1L, new Cliente(), null, 2.51, LocalDateTime.now(), null)));
				assertTrue(exception.getMessage().contains("Error while committing the transaction"));
			}
		}
	}

}
