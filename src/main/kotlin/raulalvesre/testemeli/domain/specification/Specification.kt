package raulalvesre.testemeli.domain.specification

interface Specification<T> {
    fun isSatisfiedBy(item: T): Boolean

    fun and(other: Specification<T>): Specification<T> = AndSpecification(this, other)

    fun or(other: Specification<T>): Specification<T> = OrSpecification(this, other)
}
