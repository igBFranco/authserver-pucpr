package br.pucpr.authserver.roles

import br.pucpr.authserver.users.User
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import org.intellij.lang.annotations.Pattern

@Entity
class Role(
    @Id
    @Pattern("^[A-Z][A-Z0-9]+$")
    var name: String,
    var description: String,
)
