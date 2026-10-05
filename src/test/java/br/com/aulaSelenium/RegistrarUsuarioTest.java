package br.com.aulaSelenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.aulaSelenium.pages.CadastroPage;
import br.com.aulaSelenium.pages.HomePage;
import br.com.aulaSelenium.pages.LoginPage;

/**
 * Test Case 1: Register User.
 *
 * Classes de equivalencia: nome preenchido / vazio; e-mail novo / ja cadastrado / invalido;
 * campos opcionais preenchidos / vazios.
 * Valores limite: nome e senha com 1 caractere; data de nascimento no menor valor
 * (1 / January / 1900) e no maior (31 / December / 2020) que as listas permitem.
 */
public class RegistrarUsuarioTest extends BaseTest {

	static Stream<Usuario> usuariosValidos() {
		// caso base: tudo preenchido, data de nascimento nos limites inferiores
		Usuario joao = new Usuario("Joao QA", emailUnico("joao"));
		joao.titulo = "Mr";
		joao.dia = "1";
		joao.mes = "January";
		joao.ano = "1900";
		joao.pais = "India";

		// opcionais vazios, sem marcar os checkboxes, data nos limites superiores
		Usuario maria = new Usuario("Maria QA", emailUnico("maria"));
		maria.titulo = "Mrs";
		maria.empresa = "";
		maria.endereco2 = "";
		maria.newsletter = false;
		maria.ofertas = false;
		maria.dia = "31";
		maria.mes = "December";
		maria.ano = "2020";
		maria.pais = "United States";

		// limite inferior de tamanho: nome e senha com 1 caractere
		Usuario a = new Usuario("A", emailUnico("a"));
		a.senha = "1";
		a.pais = "Canada";

		return Stream.of(joao, maria, a);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("usuariosValidos")
	public void testaRegistrarEExcluirUsuario(Usuario usuario) {
		assertTrue(home.estaVisivel(), "a pagina inicial deveria estar visivel");

		LoginPage login = home.clicarSignupLogin();
		assertTrue(login.tituloSignupVisivel(), "'New User Signup!' deveria estar visivel");

		login.preencherNovoUsuario(usuario.nome, usuario.email);
		CadastroPage cadastro = login.clicarSignup();
		assertTrue(cadastro.tituloVisivel(), "'ENTER ACCOUNT INFORMATION' deveria estar visivel");

		cadastro.preencherDadosDaConta(usuario);
		cadastro.preencherEndereco(usuario);
		cadastro.clicarCreateAccount();
		assertTrue(cadastro.contaCriadaVisivel(), "'ACCOUNT CREATED!' deveria estar visivel");

		HomePage homeLogada = cadastro.clicarContinuar();
		assertTrue(homeLogada.estaLogadoComo(usuario.nome), "deveria mostrar 'Logged in as " + usuario.nome + "'");

		homeLogada.clicarDeleteAccount();
		assertTrue(homeLogada.contaExcluidaVisivel(), "'ACCOUNT DELETED!' deveria estar visivel");
		homeLogada.clicarContinuar();
	}

	@Test
	public void testaCadastroComEmailJaCadastrado() {
		Usuario usuario = new Usuario("Repetido QA", emailUnico("repetido"));

		// 1) cria a conta normalmente
		LoginPage login = home.clicarSignupLogin();
		login.preencherNovoUsuario(usuario.nome, usuario.email);
		CadastroPage cadastro = login.clicarSignup();
		cadastro.preencherDadosDaConta(usuario);
		cadastro.preencherEndereco(usuario);
		cadastro.clicarCreateAccount();
		assertTrue(cadastro.contaCriadaVisivel(), "'ACCOUNT CREATED!' deveria estar visivel");
		HomePage homeLogada = cadastro.clicarContinuar();

		// 2) sai da conta e tenta se cadastrar de novo com o mesmo e-mail
		login = homeLogada.clicarLogout();
		assertTrue(login.tituloSignupVisivel(), "'New User Signup!' deveria estar visivel");
		login.preencherNovoUsuario("Outro Nome", usuario.email);
		login.clicarSignup();
		assertEquals("Email Address already exist!", login.mensagemErroSignup());

		// 3) entra com a conta e apaga para nao deixar lixo no site
		login.fazerLogin(usuario.email, usuario.senha);
		assertTrue(home.estaLogadoComo(usuario.nome), "deveria mostrar 'Logged in as " + usuario.nome + "'");
		home.clicarDeleteAccount();
		assertTrue(home.contaExcluidaVisivel(), "'ACCOUNT DELETED!' deveria estar visivel");
	}

	// Entradas invalidas que o proprio navegador barra (campo obrigatorio / type=email)
	@ParameterizedTest(name = "{index}: {2}")
	@CsvSource({
			"'',   novo@teste.com,  nome vazio",
			"Joao, joao.teste.com,  e-mail sem @"
	})
	public void testaCadastroBarradoPeloNavegador(String nome, String email, String descricao) {
		assertTrue(home.estaVisivel(), "a pagina inicial deveria estar visivel");

		LoginPage login = home.clicarSignupLogin();
		assertTrue(login.tituloSignupVisivel(), "'New User Signup!' deveria estar visivel");

		login.preencherNovoUsuario(nome, email);
		login.clicarSignup();

		assertFalse(login.nomeSignupEhValido() && login.emailSignupEhValido(),
				"o navegador deveria apontar um campo invalido");
		assertTrue(login.continuaNaPaginaDeLogin(), "deveria continuar na pagina de login");
		assertFalse(login.temMensagemErroSignup(), "o formulario nem deveria ter sido enviado");
	}
}
