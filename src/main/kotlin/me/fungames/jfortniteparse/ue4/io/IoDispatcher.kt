package me.fungames.jfortniteparse.ue4.io

import me.fungames.jfortniteparse.ue4.io.EIoStoreResolveResult.IoStoreResolveResult_NotFound
import me.fungames.jfortniteparse.ue4.io.EIoStoreResolveResult.IoStoreResolveResult_OK
import me.fungames.jfortniteparse.ue4.reader.FArchive
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.math.min

/**
 * I/O error code.
 */
enum class EIoErrorCode {
    Ok,
    Unknown,
    InvalidCode,
    Cancelled,
    FileOpenFailed,
    FileNotOpen,
    ReadError,
    WriteError,
    NotFound,
    CorruptToc,
    UnknownChunkID,
    InvalidParameter,
    SignatureError
}

class FIoStatusException(val errorCode: EIoErrorCode, errorMessage: String? = null, cause: Throwable? = null) : Exception(errorMessage, cause) {
    companion object {
        inline fun ok() = FIoStatusException(EIoErrorCode.Ok, "OK")
        inline fun unknown() = FIoStatusException(EIoErrorCode.Unknown, "Unknown Status")
        inline fun invalid() = FIoStatusException(EIoErrorCode.InvalidCode, "Invalid Code")
    }

    inline val isOk get() = errorCode == EIoErrorCode.Ok
    inline val isCompleted get() = errorCode != EIoErrorCode.Unknown
}

//////////////////////////////////////////////////////////////////////////

/**
 * Helper used to manage creation of I/O store file handles etc
 */
class FIoStoreEnvironment(var path: String, var order: Int = 0)

class FIoChunkHash {
    private val hash = ByteArray(32)

    constructor(Ar: FArchive) {
        Ar.read(hash)
    }
}

/**
 * Identifier to a chunk of data.
 */
class FIoChunkId {
    companion object {
        val INVALID_CHUNK_ID = createEmptyId()

        private inline fun createEmptyId() = FIoChunkId(ByteArray(12), 12)
    }

    private var id = ByteArray(12)

    constructor(id: ByteArray, size: Int) {
        check(size == 12)
        this.id = id
    }

    constructor(Ar: FArchive) {
        Ar.read(id)
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FIoChunkId

        if (!id.contentEquals(other.id)) return false

        return true
    }

    inline fun isValid() = this != INVALID_CHUNK_ID
}

/**
 * Addressable chunk types.
 */
enum class EIoChunkType {
    Invalid,
    InstallManifest,
    ExportBundleData,
    BulkData,
    OptionalBulkData,
    MemoryMappedBulkData,
    LoaderGlobalMeta,
    LoaderInitialLoadMeta,
    LoaderGlobalNames,
    LoaderGlobalNameHashes,
    ContainerHeader
}

/**
 * Creates a chunk identifier,
 */
fun createIoChunkId(chunkId: ULong, chunkIndex: UShort, ioChunkType: EIoChunkType): FIoChunkId {
    val data = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)

    data.putLong(chunkId.toLong())
    data.putShort(chunkIndex.toShort())
    data.position(11)
    data.put(ioChunkType.ordinal.toByte())

    return FIoChunkId(data.array(), 12)
}

//////////////////////////////////////////////////////////////////////////

class FIoReadOptions {
    var offset = 0uL
        private set
    var size = 0uL.inv()
        private set
    var targetVa: BytePointer? = null
    private var flags = 0

    constructor()

    constructor(offset: ULong, size: ULong) {
        this.offset = offset
        this.size = offset
    }

    fun setRange(offset: ULong, size: ULong) {
        this.offset = offset
        this.size = offset
    }
}

//////////////////////////////////////////////////////////////////////////

class FIoBatchReadOptions {
    var targetVa: BytePointer? = null
}

//////////////////////////////////////////////////////////////////////////

interface FIoRequest {
    val isOk: Boolean
    val status: FIoStatusException
    val chunkId: FIoChunkId
    fun getResultOrThrow(): BytePointer
}

interface FIoReadCallback {
    fun onSuccess(ioBuffer: BytePointer)
    fun onFailure(e: FIoStatusException)
}

enum class EIoDispatcherPriority {
    IoDispatcherPriority_Low,
    IoDispatcherPriority_Medium,
    IoDispatcherPriority_High,

    IoDispatcherPriority_Count
}

/**
 * I/O batch
 *
 * This is a primitive used to group I/O requests for synchronization
 * purposes
 */
class FIoBatch {
    var dispatcher: FIoDispatcherImpl? = null
    var impl: FIoBatchImpl? = null

    constructor()

    constructor(dispatcher: FIoDispatcherImpl, impl: FIoBatchImpl) {
        this.dispatcher = dispatcher
        this.impl = impl
    }

    fun isValid() = impl != null

    fun read(chunk: FIoChunkId, options: FIoReadOptions): FIoRequest =
        dispatcher!!.allocRequest(impl!!, chunk, options)

    fun forEachRequest(callback: (FIoRequest) -> Boolean) {
        dispatcher!!.iterateBatch(impl!!, callback)
    }

    /**
     * Initiates the loading of the batch as individual requests.
     */
    fun issue(priority: EIoDispatcherPriority) {
        dispatcher!!.issueBatch(impl!!, priority)
    }

    /**
     * Initiates the loading of the batch to a single contiguous output buffer. The requests will be in the
     * same order that they were added to the FIoBatch.
     * NOTE: It is not valid to call this on a batch containing requests that have been given a TargetVa to
     * read into as the requests are supposed to read into the batch's output buffer, doing so will cause the
     * method to return an error 'InvalidParameter'.
     *
     * @param options A set of options allowing customization on how the load will work.
     * @param callback An optional callback that will be triggered once the batch has finished loading.
     * The batch's output buffer will be provided as the parameter of the callback.
     *
     * @return This methods had the capacity to fail so the return value should be checked.
     */
    fun issueWithCallback(options: FIoBatchReadOptions, priority: EIoDispatcherPriority, callback: FIoReadCallback) {
        dispatcher!!.setupBatchForContiguousRead(impl!!, options.targetVa, callback)
    }
}

class FIoDispatcherMountedContainer(
	val environment: FIoStoreEnvironment,
	val containerId: FIoContainerId
)

/**
 * I/O dispatcher
 */
object FIoDispatcher {
    private val impl: FIoDispatcherImpl? = null

    fun mount(environment: FIoStoreEnvironment) = impl!!.mount(environment)
    fun newBatch() = FIoBatch(impl!!, impl.allocBatch())
}

class FIoDispatcherImpl {
    private val fileIoStore = FFileIoStore()
    private val waitingLock = Object()
    private var waitingRequestsHead: FIoRequestImpl? = null
    private var waitingRequestsTail: FIoRequestImpl? = null
    private val mountedContainers = Collections.synchronizedList(mutableListOf<FIoDispatcherMountedContainer>())
    private var pendingIoRequestsCount = 0uL

    fun allocRequest(chunkId: FIoChunkId, options: FIoReadOptions): FIoRequestImpl {
        val request = FIoRequestImpl()

        request.chunkId = chunkId
        request.options = options
        request.status = FIoStatusException.unknown()

        return request
    }

    fun allocRequest(batch: FIoBatchImpl, chunkId: FIoChunkId, options: FIoReadOptions): FIoRequestImpl {
        val request = allocRequest(chunkId, options)

        request.batch = batch

        if (batch.headRequest == null) {
            batch.headRequest = request
            batch.tailRequest = request
        } else {
            batch.tailRequest!!.batchNextRequest = request
            batch.tailRequest = request
        }

        check(batch.tailRequest!!.batchNextRequest == null)
        batch.unfinishedRequestsCount.getAndIncrement()

        return request
    }

    fun allocBatch() = FIoBatchImpl()

    fun onNewWaitingRequestsAdded() {
        /*if (bIsMultithreaded) {
            eventQueue.dispatcherNotify()
        } else {*/
        processIncomingRequests()
        while (pendingIoRequestsCount > 0u) {
            processCompletedRequests()
        }
        //}
    }

    fun readWithCallback(chunkId: FIoChunkId, options: FIoReadOptions, priority: EIoDispatcherPriority, callback: FIoReadCallback) {
        val request = allocRequest(chunkId, options)
        request.callback = callback
        request.priority = priority
        request.nextRequest = null
        synchronized(waitingLock) {
            if (waitingRequestsTail == null) {
                waitingRequestsTail = request
                waitingRequestsHead = waitingRequestsTail
            } else {
                waitingRequestsTail!!.nextRequest = request
                waitingRequestsTail = request
            }
        }
        onNewWaitingRequestsAdded()
    }

    fun mount(environment: FIoStoreEnvironment) {
        val containerId = fileIoStore.mount(environment)
        val mountedContainer = FIoDispatcherMountedContainer(environment, containerId)
        //eventBus?.post(ContainerMountedEvent(mountedContainer))
        mountedContainers.add(mountedContainer)
    }

    fun doesChunkExist(chunkId: FIoChunkId) = fileIoStore.doesChunkExist(chunkId)

    fun getSizeForChunk(chunkId: FIoChunkId) =
        // Only attempt to find the size if the FIoChunkId is valid
        if (chunkId.isValid()) {
            fileIoStore.getSizeForChunk(chunkId)
        } else {
            throw FIoStatusException(EIoErrorCode.InvalidParameter, "FIoChunkId is not valid")
        }

    fun iterateBatch(batch: FIoBatchImpl, inCallbackFunction: (FIoRequestImpl) -> Boolean) {
        var request = batch.headRequest

        while (request != null) {
            val bDoContinue = inCallbackFunction(request)

            request = if (bDoContinue) request.batchNextRequest else null
        }
    }

    fun issueBatch(batch: FIoBatchImpl, priority: EIoDispatcherPriority) {
        synchronized(waitingLock) {
            if (waitingRequestsHead == null) {
                waitingRequestsHead = batch.headRequest
            } else {
                waitingRequestsTail!!.nextRequest = batch.headRequest
            }
            waitingRequestsTail = batch.tailRequest
            var request = batch.headRequest
            while (request != null) {
                request.nextRequest = request.batchNextRequest
                request.priority = priority
                request = request.batchNextRequest
            }
        }
        onNewWaitingRequestsAdded()
    }

    fun setupBatchForContiguousRead(batch: FIoBatchImpl, inTargetVa: BytePointer?, inCallback: FIoReadCallback) {
        // Create the buffer
        var totalSize = 0uL
        var request = batch.headRequest
        while (request != null) {
            try {
                totalSize += min(getSizeForChunk(request.chunkId), request.options.size)
            } catch (ignored: FIoStatusException) {
            }
            request = request.batchNextRequest
        }

        // Set up memory buffers
        batch.ioBuffer = inTargetVa ?: BytePointer(totalSize.toInt())

        val dstBuffer = batch.ioBuffer

        // Now assign to each request
        val ptr = BytePointer(dstBuffer)
        var request1 = batch.headRequest
        while (request1 != null) {
            if (request1.options.targetVa != null) {
                throw FIoStatusException(EIoErrorCode.InvalidParameter, "A FIoBatch reading to a contiguous buffer cannot contain FIoRequests that have a TargetVa")
            }

            request1.options.targetVa = batch.ioBuffer

            try {
                ptr += min(getSizeForChunk(request1.chunkId), request1.options.size).toInt()
            } catch (ignored: FIoStatusException) {
            }
            request1 = request1.batchNextRequest
        }

        // Set up callback
        batch.callback = inCallback
    }

    private fun processCompletedRequests() {
        var completedRequestsHead = fileIoStore.getCompletedRequests()
        while (completedRequestsHead != null) {
            val nextRequest = completedRequestsHead.nextRequest
            completeRequest(completedRequestsHead)
            completedRequestsHead = nextRequest!!
            --pendingIoRequestsCount
        }
    }

    private fun completeRequest(request: FIoRequestImpl) {
        if (!request.status.isCompleted) {
            if (request.bFailed) {
                request.status = FIoStatusException(EIoErrorCode.ReadError)
            } else {
                request.status = FIoStatusException(EIoErrorCode.Ok)
            }
        }
        request.callback?.apply {
            if (request.status.isOk) {
                onSuccess(request.ioBuffer)
            } else {
                onFailure(request.status)
            }
        }

        request.batch?.apply {
            check(unfinishedRequestsCount.get() > 0)
            if (unfinishedRequestsCount.decrementAndGet() == 0) {
                invokeCallback(this)
            }
        } // else freeRequest(request)
    }

    private fun invokeCallback(batch: FIoBatchImpl) {
        if (batch.callback == null) {
            // No point checking if the batch does not have a callback
            return
        }

        // If there is no valid tail request then it should not have been possible to call this method
        check(batch.tailRequest != null)

        // Since the requests will be processed in order we can just check the tail request
        check(batch.tailRequest!!.status.isCompleted)

        var status = FIoStatusException(EIoErrorCode.Ok)
        // Check the requests in the batch to see if we need to report an error status
        var request = batch.headRequest
        while (request != null && status.isOk) {
            status = request.status
            request = request.batchNextRequest
        }

        // Return the buffer if there are no errors, or the failed status if there were
        if (status.isOk) {
            batch.callback!!.onSuccess(batch.ioBuffer)
        } else {
            batch.callback!!.onFailure(status)
        }
    }

    private fun processIncomingRequests() {
        var requestsToSubmitHead: FIoRequestImpl? = null
        var requestsToSubmitTail: FIoRequestImpl? = null
        while (true) {
            synchronized(waitingLock) {
                if (waitingRequestsHead != null) {
                    if (requestsToSubmitTail != null) {
                        requestsToSubmitTail!!.nextRequest = waitingRequestsHead
                        requestsToSubmitTail = waitingRequestsTail
                    } else {
                        requestsToSubmitHead = waitingRequestsHead
                        requestsToSubmitTail = waitingRequestsTail
                    }
                    waitingRequestsHead = null
                    waitingRequestsTail = null
                }
            }
            if (requestsToSubmitHead == null) {
                return
            }

            val request = requestsToSubmitHead!!
            requestsToSubmitHead = requestsToSubmitHead!!.nextRequest
            if (requestsToSubmitHead == null) {
                requestsToSubmitTail = null
            }

            // Make sure that the FIoChunkId in the request is valid before we try to do anything with it.
            if (request.chunkId.isValid()) {
                val result = fileIoStore.resolve(request)
                if (result != IoStoreResolveResult_OK) {
                    request.status = result.toStatus()
                }
            } else {
                request.status = FIoStatusException(EIoErrorCode.InvalidParameter, "FIoChunkId is not valid")
                continue
            }

            ++pendingIoRequestsCount
            request.nextRequest = null

            processCompletedRequests()
        }
    }
}

/** A utility function to convert a EIoStoreResolveResult to the corresponding FIoStatus. */
fun EIoStoreResolveResult.toStatus(): FIoStatusException {
    return when (this) {
		IoStoreResolveResult_OK -> FIoStatusException(EIoErrorCode.Ok)
		IoStoreResolveResult_NotFound -> FIoStatusException(EIoErrorCode.NotFound)
        else -> FIoStatusException(EIoErrorCode.Unknown)
    }
}

class FIoDirectoryIndexHandle private constructor(val handle: UInt) {
    companion object {
        val INVALID_HANDLE = 0u.inv()
        val ROOT_HANDLE = 0u

        @JvmStatic
        fun fromIndex(index: UInt) = FIoDirectoryIndexHandle(index)

        @JvmStatic
        fun rootDirectory() = FIoDirectoryIndexHandle(ROOT_HANDLE)

        @JvmStatic
        fun invalid() = FIoDirectoryIndexHandle(INVALID_HANDLE)
    }

    fun isValid() = handle != INVALID_HANDLE

    operator fun compareTo(other: FIoDirectoryIndexHandle) = handle.compareTo(other.handle)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FIoDirectoryIndexHandle

        if (handle != other.handle) return false

        return true
    }

    override fun hashCode() = handle.hashCode()

    fun toIndex() = handle
}

interface FIoDirectoryIndexReader {
    fun getMountPoint(): String
    fun getChildDirectory(directory: FIoDirectoryIndexHandle): FIoDirectoryIndexHandle
    fun getNextDirectory(directory: FIoDirectoryIndexHandle): FIoDirectoryIndexHandle
    fun getFile(directory: FIoDirectoryIndexHandle): FIoDirectoryIndexHandle
    fun getNextFile(directory: FIoDirectoryIndexHandle): FIoDirectoryIndexHandle
    fun getDirectoryName(directory: FIoDirectoryIndexHandle): String
    fun getFileName(file: FIoDirectoryIndexHandle): String
    fun getFileData(file: FIoDirectoryIndexHandle): UInt
}

class FIoStoreTocChunkInfo(
	val id: FIoChunkId,
	val hash: FIoChunkHash,
	val offset: ULong,
	val size: ULong,
	val bForceUncompressed: Boolean,
	val bIsMemoryMapped: Boolean,
	val bIsCompressed: Boolean
)