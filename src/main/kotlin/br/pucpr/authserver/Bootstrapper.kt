package br.pucpr.authserver

import br.pucpr.authserver.products.Product
import br.pucpr.authserver.products.ProductRepository
import br.pucpr.authserver.roles.Role
import br.pucpr.authserver.roles.RoleRepository
import br.pucpr.authserver.users.User
import br.pucpr.authserver.users.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class Bootstrapper(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        createRoles()
        createAdminUser()
        createProducts()
    }

    fun createRoles() {
        val adminRole =
            roleRepository.findByIdOrNull("ADMIN")
                ?: roleRepository.save(Role("ADMIN", "System Administrator"))
                    .also { roleRepository.save(Role("USER", "Premium User")) }
    }

    fun createAdminUser() {
        val adminRole = roleRepository.findByIdOrNull("ADMIN") ?: return

        if (userRepository.findByRole("ADMIN").isEmpty()) {
            val admin = User(
                email = "admin@authserver.com",
                password = "admin",
                name = "Auth Server Administrator"
            )
            admin.roles.add(adminRole)
            userRepository.save(admin)
        }
    }

    fun createProducts() {
        val existingProducts = productRepository.findAll().map { it.name.lowercase() }.toSet()

        val productsToCreate = listOf(
            Product(name = "Água", price = BigDecimal("2")),
            Product(name = "Café", price = BigDecimal("10"))
        ).filter { it.name.lowercase() !in existingProducts }

        if (productsToCreate.isNotEmpty()) {
            productRepository.saveAll(productsToCreate)
            println("Products Created: ${productsToCreate.joinToString { it.name }}")
        } else {
            println("Products already created.")
        }
    }

}
