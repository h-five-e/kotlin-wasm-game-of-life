import org.khronos.webgl.Uint16Array
import org.khronos.webgl.Uint8ClampedArray
import kotlin.time.measureTime

abstract class Game<T> protected constructor(
    val width: Int,
    val height: Int,
    initializer: (x: Int, y:Int) -> T,
    private val kernelMatrix: List<Pair<Int, Int>>,
) {
    protected val size = width * height

    protected var data = List(size)  {
        val x = it % width
        val y = (it - x) / width
        initializer(x,y)
    }

    private val kernelIndices = List(size) {
        val x = it % width
        val y = (it - x) / width
        kernelMatrix.map { getCellIndex(x + it.first, y + it.second) }
    }

    abstract fun update(currentCell: T, kernel: List<T>): T
    // fixme: dirty workaround for https://youtrack.jetbrains.com/issue/KT-24583
    //  should use Uint8ClampedArray
    abstract fun render(imageData: Uint16Array)

    private fun getCellIndex(x: Int, y: Int): Int =
        ((if (y < 0) y + this.height else y) % this.height) * this.width +
                ((if (x < 0) x + this.width else x) % this.width)

    private fun getKernel(i: Int): List<T> {
        val indices = kernelIndices[i]
        return List(indices.size) {
            this.data[indices[it]]
        }
    }

    fun step() {
        val timeTaken = measureTime {
            val newData = List(size) {
                this.update(this.data[it], this.getKernel(it))
            }
            this.data = newData
        }
        println(timeTaken)
    }
}