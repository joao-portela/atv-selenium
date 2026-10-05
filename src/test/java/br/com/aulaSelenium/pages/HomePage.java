package br.com.aulaSelenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Pagina inicial do site (e o cabecalho, que aparece em todas as paginas). */
public class HomePage extends PaginaBase {

	private By slider = By.id("slider-carousel");
	private By linkSignupLogin = By.cssSelector("a[href='/login']");
	private By linkLogadoComo = By.xpath("//a[contains(., 'Logged in as')]");
	private By linkDeleteAccount = By.cssSelector("a[href='/delete_account']");
	private By linkLogout = By.cssSelector("a[href='/logout']");
	private By tituloContaExcluida = By.cssSelector("h2[data-qa='account-deleted']");
	private By botaoContinuar = By.cssSelector("a[data-qa='continue-button']");

	public HomePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public boolean estaVisivel() {
		return driver.getTitle().contains("Automation Exercise") && estaVisivel(slider);
	}

	public LoginPage clicarSignupLogin() {
		clicar(linkSignupLogin);
		return new LoginPage(driver, wait);
	}

	public boolean estaLogadoComo(String nome) {
		return esperar(linkLogadoComo).getText().contains(nome);
	}

	public void clicarDeleteAccount() {
		clicar(linkDeleteAccount);
	}

	public LoginPage clicarLogout() {
		clicar(linkLogout);
		return new LoginPage(driver, wait);
	}

	public boolean contaExcluidaVisivel() {
		// o CSS do site deixa o titulo em maiusculas, por isso o equalsIgnoreCase
		return esperar(tituloContaExcluida).getText().trim().equalsIgnoreCase("Account Deleted!");
	}

	public void clicarContinuar() {
		clicar(botaoContinuar);
	}
}
