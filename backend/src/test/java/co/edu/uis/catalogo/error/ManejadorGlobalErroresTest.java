package co.edu.uis.catalogo.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@WebMvcTest(controllers = ManejadorGlobalErroresTest.ControladorDePrueba.class)
@Import(ManejadorGlobalErroresTest.ControladorDePrueba.class)
class ManejadorGlobalErroresTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void productoNoEncontradoResponde404() throws Exception {
		mockMvc.perform(get("/prueba/no-encontrado"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.codigo").value("PRODUCTO_NO_ENCONTRADO"))
				.andExpect(jsonPath("$.mensaje").value("No existe un producto con id 123"));
	}

	@Test
	void reglaDeNegocioResponde400() throws Exception {
		mockMvc.perform(get("/prueba/regla"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"))
				.andExpect(jsonPath("$.mensaje").value("La categoría 'cat-inexistente' no existe"));
	}

	@Test
	void cuerpoInvalidoResponde400() throws Exception {
		mockMvc.perform(post("/prueba/cuerpo")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\": \"\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"))
				.andExpect(jsonPath("$.mensaje").value("nombre: es obligatorio"));
	}

	@Test
	void jsonMalFormadoResponde400() throws Exception {
		mockMvc.perform(post("/prueba/cuerpo")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{esto no es json"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"));
	}

	@Test
	void parametroFueraDeRangoResponde400() throws Exception {
		mockMvc.perform(get("/prueba/parametro").param("pagina", "0"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"))
				.andExpect(jsonPath("$.mensaje").value("pagina: debe ser 1 o más"));
	}

	@Test
	void parametroDeTipoIncorrectoResponde400() throws Exception {
		mockMvc.perform(get("/prueba/parametro").param("pagina", "abc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"))
				.andExpect(jsonPath("$.mensaje").value("El parámetro 'pagina' tiene un valor inválido"));
	}

	@RestController
	@RequestMapping("/prueba")
	static class ControladorDePrueba {

		@GetMapping("/no-encontrado")
		void noEncontrado() {
			throw new ProductoNoEncontradoException("123");
		}

		@GetMapping("/regla")
		void regla() {
			throw new ValidacionFallidaException("La categoría 'cat-inexistente' no existe");
		}

		@PostMapping("/cuerpo")
		void cuerpo(@Valid @RequestBody DatosDePrueba datos) {
		}

		@GetMapping("/parametro")
		void parametro(@RequestParam @Min(value = 1, message = "debe ser 1 o más") int pagina) {
		}

	}

	record DatosDePrueba(@NotBlank(message = "es obligatorio") String nombre) {
	}

}