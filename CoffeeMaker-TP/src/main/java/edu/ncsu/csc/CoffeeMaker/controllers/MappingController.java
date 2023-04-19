package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 * @author McKenna Corn
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    /**
     * On a GET request to /ingredient, the IngredientRecipeController will
     * return /src/main/resources/templates/ingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/ingredient", "/ingredient.html" } )
    public String addIngredientForm ( final Model model ) {
        return "ingredient";
    }

    /**
     * On a GET request to /baristahomepage, the UserController will return
     * /src/main/resources/templates/baristahomepage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/baristahomepage", "/baristahomepage.html" } )
    public String baristaHomepageForm ( final Model model ) {
        return "baristahomepage";
    }

    /**
     * On a GET request to /baristaregister, the UserController will return
     * /src/main/resources/templates/baristaregister.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/baristaregister", "/baristaregister.html" } )
    public String baristaRegisterForm ( final Model model ) {
        return "baristaregister";
    }

    /**
     * On a GET request to /customerhomepage, the UserController will return
     * /src/main/resources/templates/customerhomepage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customerhomepage", "/customerhomepage.html" } )
    public String customerHomepageForm ( final Model model ) {
        return "customerhomepage";
    }

    /**
     * On a GET request to /customerlogin, the UserController will return
     * /src/main/resources/templates/customerlogin.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customerlogin", "/customerlogin.html" } )
    public String customerLoginForm ( final Model model ) {
        return "customerlogin";
    }

    /**
     * On a GET request to /customersignup, the UserController will return
     * /src/main/resources/templates/customersignup.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customersignup", "/customersignup.html" } )
    public String customerSignupForm ( final Model model ) {
        return "customersignup";
    }

    /**
     * On a GET request to /customerhomepage, the UserController will return
     * /src/main/resources/templates/managerhomepage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/managerhomepage", "/managerhomepage.html" } )
    public String managerHomepageForm ( final Model model ) {
        return "managerhomepage";
    }

    /**
     * On a GET request to /stafflogin, the UserController will return
     * /src/main/resources/templates/stafflogin.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/stafflogin", "/stafflogin.html" } )
    public String staffLoginForm ( final Model model ) {
        return "stafflogin";
    }

    /**
     * On a GET request to /orderhistory, the UserController will return
     * /src/main/resources/templates/orderhistory.html.
     *
     * @param model
     *            undrelying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/orderhistory", "/orderhistory.html" } )
    public String orderHistoryForm ( final Model model ) {
        return "orderhistory";
    }

    /**
     * On a GET request to /orderdrink, the UserController will return
     * /src/main/resources/templates/orderdrink.html.
     *
     * @param model
     *            undrelying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/orderdrink", "/orderdrink.html" } )
    public String orderBeverageForm ( final Model model ) {
        return "orderdrink";
    }

    /*
     * On a GET request to /ordersummary, the UserController will return
     * /src/main/resources/templates/ordersummary.html.
     * @param model underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/ordersummary", "/ordersummary.html" } )
    public String orderSummaryForm ( final Model model ) {
        return "ordersummary";
    }

}
