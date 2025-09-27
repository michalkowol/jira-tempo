package pl.michalkowol.nip

data class NipBulkRequestDto(
    val data: List<Nip>
)

data class NipBulkResponseDto(
    val data: List<CompanyInfo>
)

data class CompanyInfo(
    val regon: String,
    val nip: String,
    val nipStatus: String?,
    val name: String,
    val voivodeship: String,
    val county: String,
    val commune: String,
    val city: String,
    val postalCode: String,
    val street: String,
    val buildingNumber: String,
    val apartmentNumber: String?,
    val type: String,
    val silosId: String,
    val businessEndDate: String?,
    val postalCity: String
)
