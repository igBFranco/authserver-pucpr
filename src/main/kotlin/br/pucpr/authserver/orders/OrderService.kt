package br.pucpr.authserver.orders

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.products.ProductRepository
import br.pucpr.authserver.products.ProductService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val productRepository: ProductRepository,
    private val productService: ProductService
) {
    fun listAll(): List<Order> = orderRepository.findAll()

    fun findById(id: Long) = orderRepository.findById(id).orElse(null)

    fun save(order: Order) = orderRepository.save(order)

    fun createOrder(request: OrderRequest): Order {
        val products = productRepository.findAllById(request.productIds)

        if (products.isEmpty()) {
            throw BadRequestException("No valid products found for the given IDs: ${request.productIds}")
        }

        val order = Order(
            date = LocalDateTime.now(),
            products = products.toMutableList()
        )

        return orderRepository.save(order)
    }

    fun addOrder(orderId: Long, productId: Long): Boolean {
        val order = findById(orderId) ?: throw NotFoundException("Order $orderId not found")
        if (order.products.any { it.id == productId }) return false

        val product = productRepository.findById(productId).orElseThrow {
            BadRequestException("Product ID $productId not found")
        }

        order.products.add(product)
        orderRepository.save(order)
        return true
    }

    fun removeProduct(orderId: Long, productId: Long): Order? {
        val order = findById(orderId) ?: return null
        order.products.removeIf { it.id == productId }
        return orderRepository.save(order)
    }
}