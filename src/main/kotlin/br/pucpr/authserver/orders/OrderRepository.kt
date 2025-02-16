package br.pucpr.authserver.orders
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Long> {

    @Query("select distinct o from Order o" +
            " join o.products p" +
            " where p.name = :product" +
            " order by o.date")
    fun findByProduct(product: String): List<Order>
}