package br.pucpr.authserver.orders
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.users.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tblOrders")
class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToMany
    @JoinTable(
        name = "OrderProducts",
        joinColumns = [JoinColumn(name = "idOrder")],
        inverseJoinColumns = [JoinColumn(name = "idProduct")]
    )
    val products: MutableList<Product> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name= "userId")
    val user: User? = null
)
