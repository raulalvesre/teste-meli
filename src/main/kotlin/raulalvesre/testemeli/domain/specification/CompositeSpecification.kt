package raulalvesre.testemeli.domain.specification

class AndSpecification<T>(
    private val left: Specification<T>,
    private val right: Specification<T>,
) : Specification<T> {
    override fun isSatisfiedBy(item: T): Boolean = left.isSatisfiedBy(item) && right.isSatisfiedBy(item)
}

class OrSpecification<T>(
    private val left: Specification<T>,
    private val right: Specification<T>,
) : Specification<T> {
    override fun isSatisfiedBy(item: T): Boolean = left.isSatisfiedBy(item) || right.isSatisfiedBy(item)
}
