package br.pucpr.authserver.products
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

@Entity
@Table(name = "tblProducts")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotBlank
    var name: String,
    var description: String? = null,
    var price: BigDecimal,

)
