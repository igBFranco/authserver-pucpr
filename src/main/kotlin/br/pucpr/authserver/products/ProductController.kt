package br.pucpr.authserver.products

import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/products")
class ProductController(
    val productService: ProductService
) {

    @GetMapping
    fun findAll() = ResponseEntity.ok(productService.listAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        productService.findById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun insert(@RequestBody product: Product) =
        ResponseEntity.ok(productService.save(product))

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun delete(@PathVariable id: String, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val uid = if (id == "me") user.id else id.toLong()
        return if (user.isAdmin)
            productService.delete(uid).let { ResponseEntity.ok().build() }
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @GetMapping("/filtro")
    fun filter(
        @RequestParam(required = false) minPrice: BigDecimal?,
        @RequestParam(required = false) maxPrice: BigDecimal?,
        @RequestParam(defaultValue = "name") sort: String,
        @RequestParam(defaultValue = "asc") dir: String
    ): ResponseEntity<List<Product>> {
        val products = productService.listAll()
            .filter { it.price >= (minPrice ?: BigDecimal.ZERO) && it.price <= (maxPrice ?: BigDecimal.valueOf(Double.MAX_VALUE)) }
            .sortedBy { if (sort == "name") it.name else it.price.toString() }
        return ResponseEntity.ok(if (dir == "desc") products.reversed() else products)
    }

}