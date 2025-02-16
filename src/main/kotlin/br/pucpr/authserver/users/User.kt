package br.pucpr.authserver.users

import br.pucpr.authserver.roles.Role
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "tblUsers")
class User(
    @Id @GeneratedValue
    var id: Long? = null,

    @NotBlank
    var name: String,

    @NotBlank
    var password: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @ManyToMany
    @JoinTable(
        name="UserRoles",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idRole")]
    )
    val roles: MutableSet<Role> = mutableSetOf()
)
