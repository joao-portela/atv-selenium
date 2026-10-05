package br.com.aulaSelenium;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.aulaSelenium.pages.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Classe base dos testes: abre o Chrome antes de cada teste, navega para o site
 * e fecha o navegador no final.
 */
public class BaseTest {

	protected static final String URL = "https://automationexercise.com";

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected HomePage home;

	@BeforeAll
	public static void configuraDriver() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void createDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized", "--disable-notifications");
		// nao espera os anuncios do site terminarem de carregar
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		// para rodar sem abrir a janela: mvnw test -Dheadless=true
		if (Boolean.getBoolean("headless")) {
			options.addArguments("--headless=new", "--window-size=1920,1080");
		}

		driver = new ChromeDriver(options);
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		home = new HomePage(driver, wait);

		driver.get(URL);
	}

	@AfterEach
	public void quitDriver() {
		try {
			// se o teste falhou no meio do cadastro e ficou logado, apaga a conta
			// para nao deixar lixo no site
			if (!driver.findElements(By.cssSelector("a[href='/delete_account']")).isEmpty()) {
				driver.findElement(By.cssSelector("a[href='/delete_account']")).click();
			}
		} catch (Exception e) {
			// nao tem problema, o importante e fechar o navegador
		} finally {
			driver.quit();
		}
	}

	/** Se o teste falhar, salva um print da tela em target/screenshots para ajudar a entender o erro. */
	@RegisterExtension
	AfterTestExecutionCallback printSeFalhar = context -> {
		if (context.getExecutionException().isPresent() && driver != null) {
			try {
				Path pasta = Paths.get("target", "screenshots");
				Files.createDirectories(pasta);
				Path print = pasta.resolve(context.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_") + ".png");
				Files.copy(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath(), print,
						StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Teste falhou em " + driver.getCurrentUrl() + " - print salvo em " + print);
			} catch (Exception e) {
				// e so um apoio para depurar, nao pode derrubar o teste
			}
		}
	};

	/** Gera um e-mail diferente a cada execucao, para o cadastro nunca bater com um ja existente. */
	protected static String emailUnico(String prefixo) {
		return prefixo + System.currentTimeMillis() + "@teste.com";
	}
}
