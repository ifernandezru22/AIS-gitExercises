package es.codeurjc.web.nitflex.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.codeurjc.web.nitflex.Application;
import es.codeurjc.web.nitflex.model.User;
import es.codeurjc.web.nitflex.repository.UserRepository;
import es.codeurjc.web.nitflex.utils.AgeRatingOptionsUtils;


@SpringBootTest(
    classes = Application.class
    ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class seleniumTest {
    @Autowired
    private UserRepository userRepository;
    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setupTest() {
        userRepository.save(new User("FAKE USERX", "fakeUserX@gmail.com"));

        driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        
    }
    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    void newFilmCreationThenWaitForItToAppearOnScreen(){
        //Given
        driver.get("http://localhost:" + port + "/");
        
        //When
        
        String newTitle = "Title";
        String newYear = "2000";
        String newSynopsis = "Synopsis";
        String newAge = AgeRatingOptionsUtils.AgeRating.AGES_18_AND_UP.getDescription();
        fillInfo(newTitle, newYear, newSynopsis, newAge);

        //Then
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("film-title")));
        String title = driver.findElement(By.id("film-title")).getText();
        String year = driver.findElement(By.id("film-releaseYear")).getText();
        String synopsis = driver.findElement(By.id("film-synopsis")).getText();
        String age = driver.findElement(By.className("large")).getText();

        assertThat(title).isEqualTo(newTitle);
        assertThat(year).isEqualTo(newYear);
        assertThat(synopsis).isEqualTo(newSynopsis);
        assertThat(age).isEqualTo(newAge);
    }
    
    @Test
    void newFilmCreationWithoutTitleHasError() {
        //Given
        driver.get("http://localhost:" + port + "/");

        //When
        String newTitle = "";
        String newYear = "2000";
        String newSynopsis = "Synopsis";
        String newAge = AgeRatingOptionsUtils.AgeRating.AGES_18_AND_UP.getDescription();
        fillInfo(newTitle, newYear, newSynopsis, newAge);

        //Then
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("li")));
        String error = driver.findElement(By.tagName("li")).getText();
        assertThat(error).isEqualTo("The title is empty");
    }

    private void fillInfo(String newTitle, String newYear, String newSynopsis, String newAge) {
        //Click Create film button
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("create-film")));
        driver.findElement(By.id("create-film")).click();
        
        //Fill form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
        driver.findElement(By.name("title")).sendKeys(newTitle);
        driver.findElement(By.name("releaseYear")).clear();
        driver.findElement(By.name("releaseYear")).sendKeys(newYear);
        driver.findElement(By.name("synopsis")).sendKeys(newSynopsis);
        new Select(driver.findElement(By.name("ageRating"))).selectByValue(newAge);
        driver.findElement(By.id("Save")).click();
    }

}
