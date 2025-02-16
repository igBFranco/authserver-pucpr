package br.pucpr.authserver.orders

data class OrderRequest(
    val productIds: List<Long>
)
