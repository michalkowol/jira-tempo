package pl.michalkowol.nip

@JvmInline
value class Nip(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class SessionId(val value: String) {
    override fun toString(): String = value
}
