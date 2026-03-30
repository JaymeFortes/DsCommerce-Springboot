package com.DsCommerce.controllersIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.DsCommerce.dto.ProductDTO;
import com.DsCommerce.entities.Category;
import com.DsCommerce.entities.Product;
import com.DsCommerce.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest // Indica que é um teste de integração
@AutoConfigureMockMvc // Configura o MockMvc para simular requisições HTTP
@Transactional // Garante que as alterações no banco de dados sejam revertidas após cada teste
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc; // Simula requisições HTTP para os endpoints do controlador

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String clientToken, adminToken, invalidToken;

    private String productName;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        productName = "Macbook Pro";

        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword); // Obtém o token de
                                                                                            // autenticação para o
                                                                                            // usuário cliente
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword); // Obtém o token de
                                                                                         // autenticação para o usuário
                                                                                         // admin

        invalidToken = adminToken + "xpto"; // Cria um token inválido adicionando um sufixo ao token válido do admin

        Category category = new Category(2L, "Eletrônicos");
        product = new Product(null, "PlayStation 5",
                "PlayStation 5 é a próxima geração de console da Sony, oferecendo gráficos impressionantes, tempos de carregamento rápidos e uma experiência de jogo imersiva.",
                4999.99,
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg");
        product.getCategories().add(category);
        productDTO = new ProductDTO(product);

    }

    @Test
    public void findAllShouldReturnWhenNameParamIsNotEmpty() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/products?name={productName}", productName).accept(MediaType.APPLICATION_JSON)); // Simula
                                                                                                               // uma
                                                                                                               // requisição
                                                                                                               // GET
                                                                                                               // para o
                                                                                                               // endpoint
                                                                                                               // /products
                                                                                                               // com o
                                                                                                               // parâmetro
                                                                                                               // name

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(3L)); // Verifica se o ID do primeiro produto retornado é 3
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro")); // Verifica se o nome do primeiro produto
                                                                              // retornado é "MacBook"
        result.andExpect(jsonPath("$.content[0].price").value("1250.0")); // Verifica se o preço do primeiro produto
                                                                          // retornado é "1250.00"
        result.andExpect(jsonPath("$.content[0].imgUrl").value(
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
    }

    @Test
    public void findAllShouldReturnWhenNameParamIsEmpty() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/products").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(1L));
        result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
        result.andExpect(jsonPath("$.content[0].price").value("90.5"));
        result.andExpect(jsonPath("$.content[0].imgUrl").value(
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
    }

    @Test
    public void insertShouldReturnProductDTOCreatedWhenAdminLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string
                                                                       // JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken) // Substitua adminToken pelo token de
                                                                         // autenticação do usuário admin
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(26L));
        result.andExpect(jsonPath("$.name").value("PlayStation 5"));
        result.andExpect(jsonPath("$.price").value("4999.99"));
        result.andExpect(jsonPath("$.imgUrl").value(
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg"));
        result.andExpect(jsonPath("$.description").value(
                "PlayStation 5 é a próxima geração de console da Sony, oferecendo gráficos impressionantes, tempos de carregamento rápidos e uma experiência de jogo imersiva."));
        result.andExpect(jsonPath("$.categories[0].id").value(2L));
    }

    @Test
     public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {

        product.setName("ab");
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                .header("Authorization", "Bearer " + adminToken) // Substitua adminToken pelo token de autenticação do usuário admin
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

                result.andExpect(status().isUnprocessableEntity());
     }

     @Test
     public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {

        product.setDescription("ab");
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                .header("Authorization", "Bearer " + adminToken) // Substitua adminToken pelo token de autenticação do usuário admin
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

                result.andExpect(status().isUnprocessableEntity());
     }

     @Test
     public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {

        product.setPrice(-10.00);
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                .header("Authorization", "Bearer " + adminToken) // Substitua adminToken pelo token de autenticação do usuário admin
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

                result.andExpect(status().isUnprocessableEntity());
     }

     @Test
     public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {

        product.setPrice(0.00);
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                .header("Authorization", "Bearer " + adminToken) // Substitua adminToken pelo token de autenticação do usuário admin
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

                result.andExpect(status().isUnprocessableEntity());
     }

     @Test
     public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndProductHasNoCategory() throws Exception {

        product.getCategories().clear();
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                .header("Authorization", "Bearer " + adminToken) // Substitua adminToken pelo token de autenticação do usuário admin
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

                result.andExpect(status().isUnprocessableEntity());
     }

     @Test
     public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + clientToken) // Substitua clientToken pelo token de autenticação do usuário cliente
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isForbidden());
     }

     @Test
     public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO); // Converte o objeto productDTO para uma string JSON

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + invalidToken) // Substitua invalidToken pelo token de autenticação inválido
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
     }
}

