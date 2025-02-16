package br.pucpr.authserver.products

import org.springframework.http.ResponseEntity
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
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
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