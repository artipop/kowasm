/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kowasm.wasi.internal

import org.kowasm.wasi.*
import kotlin.wasm.unsafe.*
import kotlin.wasm.WasmImport

/**
 * Standard file descriptors.
 */
internal enum class StandardFileDescriptor {
    /** Standard input file descriptor. */
    STDIN,
    /** Standard output file descriptor. */
    STDOUT,
    /** Standard error file descriptor. */
    STDERR,
    /** First preopen file descriptor. */
    FIRST_PREOPEN,
}

internal typealias LookupFlags = Int

object LookupFlag {
    /** As long as the resolved path corresponds to a symbolic link, it is expanded. */
    const val SYMLINK_FOLLOW: LookupFlags = 1 shl 0
}

internal typealias OFlags = Short

object OFlag {
    /** Create file if it does not exist. */
    const val CREAT: OFlags = (1 shl 0).toShort()
    /** Fail if not a directory. */
    const val DIRECTORY: OFlags = (1 shl 1).toShort()
    /** Fail if file already exists. */
    const val EXCL: OFlags = (1 shl 2).toShort()
    /** Truncate file to size 0. */
    const val TRUNC: OFlags = (1 shl 3).toShort()
}

internal typealias Rights = Long

object Right {
    /**
     * The right to invoke `fd_datasync`. If `path_open` is set, includes the right to invoke
     * `path_open` with `fdflags::dsync`.
     */
    const val FD_DATASYNC: Rights = 1 shl 0
    /**
     * The right to invoke `fd_read` and `sock_recv`. If `rights::fd_seek` is set, includes the right
     * to invoke `fd_pread`.
     */
    const val FD_READ: Rights = 1 shl 1
    /** The right to invoke `fd_seek`. This flag implies `rights::fd_tell`. */
    const val FD_SEEK: Rights = 1 shl 2
    /** The right to invoke `fd_fdstat_set_flags`. */
    const val FD_FDSTAT_SET_FLAGS: Rights = 1 shl 3
    /**
     * The right to invoke `fd_sync`. If `path_open` is set, includes the right to invoke `path_open`
     * with `fdflags::rsync` and `fdflags::dsync`.
     */
    const val FD_SYNC: Rights = 1 shl 4
    /**
     * The right to invoke `fd_seek` in such a way that the file offset remains unaltered (i.e.,
     * `whence::cur` with offset zero), or to invoke `fd_tell`.
     */
    const val FD_TELL: Rights = 1 shl 5
    /**
     * The right to invoke `fd_write` and `sock_send`. If `rights::fd_seek` is set, includes the right
     * to invoke `fd_pwrite`.
     */
    const val FD_WRITE: Rights = 1 shl 6
    /** The right to invoke `fd_advise`. */
    const val FD_ADVISE: Rights = 1 shl 7
    /** The right to invoke `fd_allocate`. */
    const val FD_ALLOCATE: Rights = 1 shl 8
    /** The right to invoke `path_create_directory`. */
    const val PATH_CREATE_DIRECTORY: Rights = 1 shl 9
    /** If `path_open` is set, the right to invoke `path_open` with `oflags::creat`. */
    const val PATH_CREATE_FILE: Rights = 1 shl 10
    /** The right to invoke `path_link` with the file descriptor as the source directory. */
    const val PATH_LINK_SOURCE: Rights = 1 shl 11
    /** The right to invoke `path_link` with the file descriptor as the target directory. */
    const val PATH_LINK_TARGET: Rights = 1 shl 12
    /** The right to invoke `path_open`. */
    const val PATH_OPEN: Rights = 1 shl 13
    /** The right to invoke `fd_readdir`. */
    const val FD_READDIR: Rights = 1 shl 14
    /** The right to invoke `path_readlink`. */
    const val PATH_READLINK: Rights = 1 shl 15
    /** The right to invoke `path_rename` with the file descriptor as the source directory. */
    const val PATH_RENAME_SOURCE: Rights = 1 shl 16
    /** The right to invoke `path_rename` with the file descriptor as the target directory. */
    const val PATH_RENAME_TARGET: Rights = 1 shl 17
    /** The right to invoke `path_filestat_get`. */
    const val PATH_FILESTAT_GET: Rights = 1 shl 18
    /**
     * The right to change a file's size (there is no `path_filestat_set_size`). If `path_open` is
     * set, includes the right to invoke `path_open` with `oflags::trunc`.
     */
    const val PATH_FILESTAT_SET_SIZE: Rights = 1 shl 19
    /** The right to invoke `path_filestat_set_times`. */
    const val PATH_FILESTAT_SET_TIMES: Rights = 1 shl 20
    /** The right to invoke `fd_filestat_get`. */
    const val FD_FILESTAT_GET: Rights = 1 shl 21
    /** The right to invoke `fd_filestat_set_size`. */
    const val FD_FILESTAT_SET_SIZE: Rights = 1 shl 22
    /** The right to invoke `fd_filestat_set_times`. */
    const val FD_FILESTAT_SET_TIMES: Rights = 1 shl 23
    /** The right to invoke `path_symlink`. */
    const val PATH_SYMLINK: Rights = 1 shl 24
    /** The right to invoke `path_remove_directory`. */
    const val PATH_REMOVE_DIRECTORY: Rights = 1 shl 25
    /** The right to invoke `path_unlink_file`. */
    const val PATH_UNLINK_FILE: Rights = 1 shl 26
    /**
     * If `rights::fd_read` is set, includes the right to invoke `poll_oneoff` to subscribe to
     * `eventtype::fd_read`. If `rights::fd_write` is set, includes the right to invoke `poll_oneoff`
     * to subscribe to `eventtype::fd_write`.
     */
    const val POLL_FD_READWRITE: Rights = 1 shl 27
    /** The right to invoke `sock_shutdown`. */
    const val SOCK_SHUTDOWN: Rights = 1 shl 28
    /** The right to invoke `sock_accept`. */
    const val SOCK_ACCEPT: Rights = 1 shl 29
}

internal typealias Fdflags = Short

object FdFlag {
    /** Append mode: Data written to the file is always appended to the file's end. */
    const val APPEND: Fdflags = (1 shl 0).toShort()
    /**
     * Write according to synchronized I/O data integrity completion. Only the data stored in the file
     * is synchronized.
     */
    const val DSYNC: Fdflags = (1 shl 1).toShort()
    /** Non-blocking mode. */
    const val NONBLOCK: Fdflags = (1 shl 2).toShort()
    /** Synchronized read I/O operations. */
    const val RSYNC: Fdflags = (1 shl 3).toShort()
    /**
     * Write according to synchronized I/O file integrity completion. In addition to synchronizing the
     * data stored in the file, the implementation may also synchronously update the file's metadata.
     */
    const val SYNC: Fdflags = (1 shl 4).toShort()
}

internal fun fdWrite(fd: Fd, ivos: List<ByteArray>): Size {
    withScopedMemoryAllocator { allocator ->
        val iovs = ivos.map { UnsafeCiovec(allocator.writeToLinearMemory(it), it.size) }
        val pointer = allocator.allocate(4)
        val returnCode =
            rawFdWrite(
                fd,
                allocator.writeToLinearMemory(iovs).address.toInt(),
                iovs.size,
                pointer.address.toInt())
        return if (returnCode == 0) {
            (Pointer(pointer.address.toInt().toUInt())).loadInt()
        } else {
            throw WasiError(Errno.values()[returnCode])
        }
    }
}

internal fun pathCreateDirectory(fd: Fd, path: String) {
    withScopedMemoryAllocator { allocator ->
        val pathByteArray = path.encodeToByteArray()
        val returnCode = rawPathCreateDirectory(fd, allocator.writeToLinearMemory(pathByteArray).address.toInt(), pathByteArray.size)
        return if (returnCode == 0) {
            Unit
        } else {
            throw WasiError(Errno.values()[returnCode])
        }
    }
}

internal fun pathOpen(
    fd: Fd,
    dirflags: LookupFlags,
    path: String,
    oflags: OFlags,
    fsRightsBase: Rights,
    fsRightsInheriting: Rights,
    fdflags: Fdflags,
): Fd {
    withScopedMemoryAllocator { allocator ->
        val pointer = allocator.allocate(4)
        val pathByteArray = path.encodeToByteArray()
        val returnCode =
            rawPathOpen(
                fd,
                dirflags,
                allocator.writeToLinearMemory(pathByteArray).address.toInt(),
                pathByteArray.size,
                oflags.toInt(),
                fsRightsBase,
                fsRightsInheriting,
                fdflags.toInt(),
                pointer.address.toInt())
        return if (returnCode == 0) {
            (Pointer(pointer.address.toInt().toUInt())).loadInt()
        } else {
            throw WasiError(Errno.values()[returnCode])
        }
    }
}

/** Write to a file descriptor. Note: This is similar to `writev` in POSIX. */
@WasmImport("wasi_snapshot_preview1", "fd_write")
private external fun rawFdWrite(
    arg0: Int,
    arg1: Int,
    arg2: Int,
    arg3: Int,
): Int

/** Create a directory. Note: This is similar to `mkdirat` in POSIX. */
@WasmImport("wasi_snapshot_preview1", "path_create_directory")
private external fun rawPathCreateDirectory(
    arg0: Int,
    arg1: Int,
    arg2: Int,
): Int

/**
 * Open a file or directory. The returned file descriptor is not guaranteed to be the
 * lowest-numbered file descriptor not currently open; it is randomized to prevent applications from
 * depending on making assumptions about indexes, since this is error-prone in multi-threaded
 * contexts. The returned file descriptor is guaranteed to be less than 2**31. Note: This is similar
 * to `openat` in POSIX.
 */
@WasmImport("wasi_snapshot_preview1", "path_open")
private external fun rawPathOpen(
    arg0: Int,
    arg1: Int,
    arg2: Int,
    arg3: Int,
    arg4: Int,
    arg5: Long,
    arg6: Long,
    arg7: Int,
    arg8: Int,
): Int