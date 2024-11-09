package UITests;

import POM.page.*;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import static com.codeborne.selenide.logevents.SelenideLogger.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static properties.GetProperties.getProperty;

public class SwagLabsTests {

    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompletePage completePage;

    @BeforeAll
    public static void setUpGlobal() {
        Configuration.baseUrl = getProperty("urlUI");
    }

    @BeforeEach
    public void setUp() {
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        cartPage = new CartPage();
        checkoutPage = new CheckoutPage();
        completePage = new CompletePage();

    }

    @Test
    @DisplayName("Успешная авторизация")
    @Tag("positive")
    public void successfulLogin() {
        loginPage.login(getProperty("standardUser"), getProperty("passwordUI"));
        productsPage.checkOfSuccessfulLogin();
    }

    @Test
    @DisplayName("Авторизация заблокированного пользователя")
    @Tag("negative")
    public void unsuccessfulLogin() {
        loginPage.login(getProperty("blockedUser"), getProperty("passwordUI"));
        step("Проверяем что вышло сообщение о том, что пользователь заблокирован", () -> assertEquals("Epic sadface: Sorry, this user has been locked out.", loginPage.getErrorText()));
    }

    @Test
    @DisplayName("e2e-сценарий под пользователем standard_user")
    @Tag("positive")
    public void e2eStandardUser() {
        loginPage.login(getProperty("standardUser"), getProperty("passwordUI"));
        productsPage.addToCart();
        productsPage.header.goToCart();
        cartPage.checkItemsInCartNumberShouldBe(3);
        cartPage.checkout();
        checkoutPage.fillOutCheckoutForm(getProperty("firstName"), getProperty("lastName"), getProperty("zip"));
        checkoutPage.checkOfTotalPrice();
        checkoutPage.finish();
        completePage.checkComplete();
    }

    @Test
    @DisplayName("e2e-сценарий под пользователем performance_glitch_user")
    @Tag("positive")
    public void e2ePerformanceGlitchUser() {
        loginPage.login(getProperty("performanceGlitchUser"), getProperty("passwordUI"));
        productsPage.checkOfSuccessfulLogin();
        productsPage.addToCart();
        productsPage.header.goToCart();
        cartPage.checkItemsInCartNumberShouldBe(3);
        cartPage.checkout();
        checkoutPage.fillOutCheckoutForm(getProperty("firstName"), getProperty("lastName"), getProperty("zip"));
        checkoutPage.checkOfTotalPrice();
        checkoutPage.finish();
        completePage.checkComplete();
    }

}
