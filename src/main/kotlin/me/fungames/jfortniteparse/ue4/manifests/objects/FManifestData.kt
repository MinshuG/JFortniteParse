package me.fungames.jfortniteparse.ue4.manifests.objects

import me.fungames.jfortniteparse.compression.Compression
import me.fungames.jfortniteparse.ue4.UClass
import me.fungames.jfortniteparse.ue4.pak.CompressionMethod
import me.fungames.jfortniteparse.ue4.reader.FArchive
import me.fungames.jfortniteparse.ue4.reader.FByteArchive

@ExperimentalUnsignedTypes
class FManifestData : UClass {

    var header : FManifestHeader
    var meta : FManifestMeta
    var chunkDataList : FChunkDataList
    var fileManifestList : FFileManifestList
    var customFields : FCustomFields


    constructor(Ar : FArchive) {
        super.init(Ar)
        val startPos = Ar.pos()
        header = FManifestHeader(Ar)
        var manifestRawData = Ar.read(header.dataSizeCompressed.toInt())
        if (header.storedAs.toInt() and 1 != 0) {
            //Compressed, only compression format is ZLib
            manifestRawData = Compression.decompress(manifestRawData, header.dataSizeUncompressed.toInt(), CompressionMethod.Zlib)
        }
        val rawAr = FByteArchive(manifestRawData)
        meta = FManifestMeta(rawAr)
        chunkDataList = FChunkDataList(rawAr)
        fileManifestList = FFileManifestList(rawAr)
        customFields = FCustomFields(rawAr)
        Ar.seek(startPos + header.headerSize.toInt() + header.dataSizeCompressed.toInt())
        super.complete(Ar)
    }

    fun serialize(Ar : FArchive) {

    }
}