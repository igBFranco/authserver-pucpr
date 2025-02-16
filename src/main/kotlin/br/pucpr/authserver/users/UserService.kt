package br.pucpr.authserver.users

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.roles.RoleRepository
import br.pucpr.authserver.roles.RoleService
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    private val roleService: RoleService
) {
    fun insert(user: User)= userRepository.save(user)
    fun findAll(dir: SortDir, role: String?): List<User> {
        if (!role.isNullOrBlank())
            return userRepository.findByRole(role)
        return when(dir) {
            SortDir.ASC -> userRepository.findAll(Sort.by("name"))
            SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
        }
    }


    fun findByIdOrNull(id: Long) =
        userRepository.findByIdOrNull(id)
    fun delete(id: Long) = userRepository.deleteById(id)

    fun addRole(id: Long, roleName: String): Boolean {
        val roleUpper = roleName.uppercase()
        val user = findByIdOrNull(id) ?: throw NotFoundException("User ${id} not found")
        if (user.roles.any { it.name ==  roleUpper }) return false

        val role = roleService.findByNameOrNull(roleUpper) ?: throw BadRequestException("Role ${roleUpper} not found")

        user.roles.add(role)
        userRepository.save(user)
        return true
    }
}
