package controller;

import controller.visitorsystem.VisitorMenuView;
import view.component.Label;
import view.controller.NavigationViewController;
import view.views.MultipleChoiceView;
import view.views.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The class that extends View display the start menu
 */
public class StartMenuSystem extends View {
    private int failCount = 0;

    /**
     * override the view method to display options of the start menu
     */
    @Override
    public void view() {
        component(
                new Label("Welcome to the start menu.")
        );
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("1","Type '1' to login", this::login)
                        .addQuestion("2", "Type '2' to sign up", this::signup)
                        .addQuestion("3", "Type '3' to look around", () -> redirect(new VisitorMenuView()))
                        .addQuestion("exit", "Type 'exit' To exit the system", () -> System.exit(0))
                        .setError(comm -> {
                                if (failCount++ == 2) {
                                    component(
                                            new Label("Having trouble? Please contact us at ...")
                                    );
                                }
                                if (failCount == 5) {
                                    component(
                                            new Label("Sorry! You tried too many times...")
                                    );
                                    System.exit(0);
                                }
                                System.out.printf("\nYour input \"%s\" is not a valid input.\n",comm);
                                return true;
                        })
                        .hideKey(true)
                .build()
        );
    }

    /**
     * the method to run
     */
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        NavigationViewController viewController = new NavigationViewController();
        viewController.setRootView(this);
        viewController.run(reader);
    }

    /**
     * the method to login
     */
    private void login() {
        try {
            LoginSystem loginSystem = new LoginSystem();
            loginSystem.run();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    /**
     * the method to sign up
     */
    private void signup() {
        try {
            SignUpSystem signUpSystem = new SignUpSystem();
            signUpSystem.run();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    /**
     * inherit the method rebuild and set failCount into 0
     */
    @Override
    public void rebuild() {
        super.rebuild();
        failCount = 0;
    }
}
