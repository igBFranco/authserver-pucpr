package br.pucpr.authserver.orders

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    val orderService: OrderService
) {
    @GetMapping
    fun listAll() = ResponseEntity.ok(orderService.listAll())

    @PostMapping
    fun create(@RequestBody request: OrderRequest) =
        ResponseEntity.ok(orderService.createOrder(request))

    @PostMapping("/{orderId}/products/{productId}")
    fun addProduct(@PathVariable orderId: Long, @PathVariable productId: Long) =
        orderService.addOrder(orderId, productId).let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{orderId}/products/{productId}")
    fun removeProduct(@PathVariable orderId: Long, @PathVariable productId: Long) =
        orderService.removeProduct(orderId, productId)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}