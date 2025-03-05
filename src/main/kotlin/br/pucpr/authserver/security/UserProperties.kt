package br.pucpr.authserver.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "security.admin")
data class UserProperties @ConstructorBinding constructor(
    val name: String,
    val email: String,
    val password: String,
)

@ConfigurationProperties(prefix = "security.user")
data class NormalUserProperties @ConstructorBinding constructor(
    val name: String,
    val email: String,
    val password: String,
)

