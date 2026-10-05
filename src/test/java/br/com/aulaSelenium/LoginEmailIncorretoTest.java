package br.com.aulaSelenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.aulaSelenium.pages.LoginPage;

/**
 * Test Case 3: Login User with incorrect email and password.
 *
 * Classes de equivalencia do e-mail: valido mas nao cadastrado, vazio, sem "@".
 * Classes da senha: errada (nao vazia), vazia.
 * Valores limite: e-mail com 3 caracteres (a@b, o menor que o navegador aceita)
 * e com 254 (maximo da RFC); senha com 1 e com 100 caracteres.
 */
public class LoginEmailIncorretoTest extends BaseTest {

	private static final String SENHA = "Senha@123";

	// Entradas que o navegador aceita e vao ate o servidor -> deve aparecer a mensagem de erro
	static Stream<Arguments> credenciaisIncorretas() {
		String emailNaoCadastrado = emailUnico("naoexiste");
		// 64 caracteres antes do @ e 189 depois = 254 no total
		String emailMaximo = "a".repeat(64) + "@" + "b".repeat(63) + "." + "c".repeat(63) + "." + "d".repeat(57) + ".com";

		return Stream.of(
				Arguments.of(emailNaoCadastrado, SENHA, "e-mail valido nao cadastrado"),
				Arguments.of("a@b", SENHA, "e-mail no tamanho minimo (3 caracteres)"),
				Arguments.of(emailMaximo, SENHA, "e-mail no tamanho maximo (254 caracteres)"),
				Arguments.of(emailNaoCadastrado, "1", "senha com 1 caractere"),
				Arguments.of(emailNaoCadastrado, "x".repeat(100), "senha com 100 caracteres"));
	}

	@ParameterizedTest(name = "{index}: {2}")
	@MethodSource("credenciaisIncorretas")
	public void testaLoginComEmailESenhaIncorretos(String email, String senha, String descricao) {
		assertTrue(home.estaVisivel(), "a pagina inicial deveria estar visivel");

		LoginPage login = home.clicarSignupLogin();
		assertTrue(login.tituloLoginVisivel(), "'Login to your account' deveria estar visivel");

		login.fazerLogin(email, senha);

		assertEquals("Your email or password is incorrect!", login.mensagemErroLogin());
	}

	// Entradas invalidas que o proprio navegador barra (campo obrigatorio / type=email),
	// entao o formulario nem e enviado e nao aparece a mensagem do servidor
	static Stream<Arguments> credenciaisBarradasPeloNavegador() {
		return Stream.of(
				Arguments.of("", SENHA, "e-mail vazio"),
				Arguments.of("emailsemarroba.com", SENHA, "e-mail sem @"),
				Arguments.of("naoexiste@teste.com", "", "senha vazia"));
	}

	@ParameterizedTest(name = "{index}: {2}")
	@MethodSource("credenciaisBarradasPeloNavegador")
	public void testaLoginBarradoPeloNavegador(String email, String senha, String descricao) {
		assertTrue(home.estaVisivel(), "a pagina inicial deveria estar visivel");

		LoginPage login = home.clicarSignupLogin();
		assertTrue(login.tituloLoginVisivel(), "'Login to your account' deveria estar visivel");

		login.fazerLogin(email, senha);

		assertFalse(login.emailLoginEhValido() && login.senhaLoginEhValida(),
				"o navegador deveria apontar um campo invalido");
		assertTrue(login.continuaNaPaginaDeLogin(), "deveria continuar na pagina de login");
		assertFalse(login.temMensagemErroLogin(), "o formulario nem deveria ter sido enviado");
	}
}
