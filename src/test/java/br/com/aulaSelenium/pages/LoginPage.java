package br.com.aulaSelenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Pagina /login, que tem os dois formularios: login e cadastro de novo usuario. */
public class LoginPage extends PaginaBase {

	// formulario de login
	private By tituloLogin = By.xpath("//h2[normalize-space()='Login to your account']");
	private By campoEmailLogin = By.cssSelector("input[data-qa='login-email']");
	private By campoSenhaLogin = By.cssSelector("input[data-qa='login-password']");
	private By botaoLogin = By.cssSelector("button[data-qa='login-button']");
	private By mensagemErroLogin = By.cssSelector("form[action='/login'] p");

	// formulario de novo usuario
	private By tituloSignup = By.xpath("//h2[normalize-space()='New User Signup!']");
	private By campoNomeSignup = By.cssSelector("input[data-qa='signup-name']");
	private By campoEmailSignup = By.cssSelector("input[data-qa='signup-email']");
	private By botaoSignup = By.cssSelector("button[data-qa='signup-button']");
	private By mensagemErroSignup = By.cssSelector("form[action='/signup'] p");

	public LoginPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public boolean tituloLoginVisivel() {
		return estaVisivel(tituloLogin);
	}

	public boolean tituloSignupVisivel() {
		return estaVisivel(tituloSignup);
	}

	public void fazerLogin(String email, String senha) {
		preencher(campoEmailLogin, email);
		preencher(campoSenhaLogin, senha);
		clicar(botaoLogin);
	}

	public String mensagemErroLogin() {
		return esperar(mensagemErroLogin).getText().trim();
	}

	public boolean temMensagemErroLogin() {
		return !driver.findElements(mensagemErroLogin).isEmpty();
	}

	public void preencherNovoUsuario(String nome, String email) {
		preencher(campoNomeSignup, nome);
		preencher(campoEmailSignup, email);
	}

	public CadastroPage clicarSignup() {
		clicar(botaoSignup);
		return new CadastroPage(driver, wait);
	}

	public String mensagemErroSignup() {
		return esperar(mensagemErroSignup).getText().trim();
	}

	public boolean temMensagemErroSignup() {
		return !driver.findElements(mensagemErroSignup).isEmpty();
	}

	public boolean continuaNaPaginaDeLogin() {
		return driver.getCurrentUrl().contains("/login");
	}

	// validacao HTML5 dos campos (o navegador barra antes de enviar)
	public boolean emailLoginEhValido() {
		return campoEhValido(campoEmailLogin);
	}

	public boolean senhaLoginEhValida() {
		return campoEhValido(campoSenhaLogin);
	}

	public boolean nomeSignupEhValido() {
		return campoEhValido(campoNomeSignup);
	}

	public boolean emailSignupEhValido() {
		return campoEhValido(campoEmailSignup);
	}
}
