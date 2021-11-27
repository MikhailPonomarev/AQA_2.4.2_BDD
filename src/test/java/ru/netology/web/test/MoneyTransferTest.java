package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {
    private String firstAccount = "5559 0000 0000 0001";
    private String secondAccount = "5559 0000 0000 0002";

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage.validLogin(authInfo);

        var verificationPage = new VerificationPage();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void transactionsBetweenCards() {
        var dashboardPage = new DashboardPage();

        int firstCardInitBalance = dashboardPage.getFirstCardBalance();
        int secondCardInitBalance = dashboardPage.getSecondCardBalance();

        if (firstCardInitBalance == secondCardInitBalance) {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction("1000", secondAccount);
        } else if (firstCardInitBalance > secondCardInitBalance) {
            dashboardPage.secondCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction("5000", firstAccount);
        } else if (firstCardInitBalance == 0) {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction("10000", secondAccount);
        } else if (secondCardInitBalance == 0) {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction("10000", firstAccount);
        } else {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction(String.valueOf(secondCardInitBalance), secondAccount);
        }
    }
}

