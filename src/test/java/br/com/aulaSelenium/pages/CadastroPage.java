package br.com.aulaSelenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.aulaSelenium.Usuario;

/** Pagina /signup com o formulario "Enter Account Information" e a tela "Account Created!". */
public class CadastroPage extends PaginaBase {

	private By titulo = By.xpath("//b[normalize-space()='Enter Account Information']");
	private By tituloMr = By.id("id_gender1");
	private By tituloMrs = By.id("id_gender2");
	private By campoSenha = By.cssSelector("input[data-qa='password']");
	private By selectDia = By.cssSelector("select[data-qa='days']");
	private By selectMes = By.cssSelector("select[data-qa='months']");
	private By selectAno = By.cssSelector("select[data-qa='years']");
	private By checkNewsletter = By.id("newsletter");
	private By checkOfertas = By.id("optin");

	private By campoPrimeiroNome = By.cssSelector("input[data-qa='first_name']");
	private By campoSobrenome = By.cssSelector("input[data-qa='last_name']");
	private By campoEmpresa = By.cssSelector("input[data-qa='company']");
	private By campoEndereco = By.cssSelector("input[data-qa='address']");
	private By campoEndereco2 = By.cssSelector("input[data-qa='address2']");
	private By selectPais = By.cssSelector("select[data-qa='country']");
	private By campoEstado = By.cssSelector("input[data-qa='state']");
	private By campoCidade = By.cssSelector("input[data-qa='city']");
	private By campoCep = By.cssSelector("input[data-qa='zipcode']");
	private By campoCelular = By.cssSelector("input[data-qa='mobile_number']");
	private By botaoCreateAccount = By.cssSelector("button[data-qa='create-account']");

	private By tituloContaCriada = By.cssSelector("h2[data-qa='account-created']");
	private By botaoContinuar = By.cssSelector("a[data-qa='continue-button']");

	public CadastroPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public boolean tituloVisivel() {
		return estaVisivel(titulo);
	}

	/** Passos 9, 10 e 11 do caso de teste: titulo, senha, data de nascimento e os dois checkboxes. */
	public void preencherDadosDaConta(Usuario usuario) {
		clicar(usuario.titulo.equals("Mr") ? tituloMr : tituloMrs);
		preencher(campoSenha, usuario.senha);
		new Select(esperar(selectDia)).selectByVisibleText(usuario.dia);
		new Select(esperar(selectMes)).selectByVisibleText(usuario.mes);
		new Select(esperar(selectAno)).selectByVisibleText(usuario.ano);
		if (usuario.newsletter) {
			clicar(checkNewsletter);
		}
		if (usuario.ofertas) {
			clicar(checkOfertas);
		}
	}

	/** Passo 12: dados de endereco. */
	public void preencherEndereco(Usuario usuario) {
		preencher(campoPrimeiroNome, usuario.primeiroNome);
		preencher(campoSobrenome, usuario.sobrenome);
		preencher(campoEmpresa, usuario.empresa);
		preencher(campoEndereco, usuario.endereco);
		preencher(campoEndereco2, usuario.endereco2);
		new Select(esperar(selectPais)).selectByVisibleText(usuario.pais);
		preencher(campoEstado, usuario.estado);
		preencher(campoCidade, usuario.cidade);
		preencher(campoCep, usuario.cep);
		preencher(campoCelular, usuario.celular);
	}

	public void clicarCreateAccount() {
		clicar(botaoCreateAccount);
	}

	public boolean contaCriadaVisivel() {
		return esperar(tituloContaCriada).getText().trim().equalsIgnoreCase("Account Created!");
	}

	public HomePage clicarContinuar() {
		clicar(botaoContinuar);
		return new HomePage(driver, wait);
	}
}
