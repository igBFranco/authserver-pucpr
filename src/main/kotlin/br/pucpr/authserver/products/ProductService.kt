package br.pucpr.authserver.products

import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository
) {
    fun listAll(): List<Product> = productRepository.findAll()

    fun findById(id: Long): Product = productRepository.findById(id).orElse(null)

    fun save(product: Product) = productRepository.save(product)

    fun delete(id: Long) = productRepository.deleteById(id)
}