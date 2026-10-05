package br.com.aulaSelenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Metodos que todas as paginas usam. */
public class PaginaBase {

	protected WebDriver driver;
	protected WebDriverWait wait;

	public PaginaBase(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	protected WebElement esperar(By by) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	protected boolean estaVisivel(By by) {
		try {
			return esperar(by).isDisplayed();
		} catch (TimeoutException e) {
			return false;
		}
	}

	protected void clicar(By by) {
		WebElement elemento = wait.until(ExpectedConditions.elementToBeClickable(by));
		try {
			elemento.click();
		} catch (ElementClickInterceptedException e) {
			// os anuncios do site as vezes ficam por cima do botao, entao clica via JS
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", elemento);
		}
	}

	protected void preencher(By by, String texto) {
		WebElement campo = esperar(by);
		campo.clear();
		if (!texto.isEmpty()) {
			campo.sendKeys(texto);
		}
	}

	/**
	 * Pergunta ao navegador se o campo passa na validacao HTML5 (required, type=email...).
	 * Quando nao passa, o formulario nem chega a ser enviado para o servidor.
	 */
	protected boolean campoEhValido(By by) {
		WebElement campo = driver.findElement(by);
		return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", campo);
	}
}
