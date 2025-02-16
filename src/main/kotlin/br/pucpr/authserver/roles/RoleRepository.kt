package br.pucpr.authserver.roles

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, String>
