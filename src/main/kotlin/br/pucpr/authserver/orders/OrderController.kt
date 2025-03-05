package br.pucpr.authserver.orders

import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    val orderService: OrderService
) {
    @GetMapping
    fun listAll() = ResponseEntity.ok(orderService.listAll())

    @PostMapping
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun create(@RequestBody request: OrderRequest, auth: Authentication): ResponseEntity<Order> {
        val user = auth.principal as UserToken
        val order = orderService.createOrder(request, user.id)
        return ResponseEntity.ok(order)
    }

    @PostMapping("/{orderId}/products/{productId}")
    fun addProduct(@PathVariable orderId: Long, @PathVariable productId: Long) =
        orderService.addOrder(orderId, productId).let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{orderId}/products/{productId}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun removeProduct(@PathVariable orderId: Long, @PathVariable productId: Long) =
        orderService.removeProduct(orderId, productId)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{orderId}")
    @SecurityRequirement(name = "AuthServer")
    fun deleteOrder(@PathVariable orderId: Long, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val order = orderService.findById(orderId) ?: return ResponseEntity.notFound().build()

        return if (order.user?.id == user.id || user.isAdmin) {
            orderService.delete(orderId)
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(403).build()
        }
    }

}