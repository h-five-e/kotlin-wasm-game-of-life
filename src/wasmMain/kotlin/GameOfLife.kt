import org.khronos.webgl.Uint16Array
import org.khronos.webgl.set
import kotlin.random.Random

private val kernelMatrix = listOf(
    Pair(-1, -1), Pair( 0, -1), Pair( 1, -1),
    Pair(-1,  0),               Pair( 1,  0),
    Pair(-1,  1), Pair( 0,  1), Pair( 1,  1),
)

class GameOfLife(
    width: Int,
    height: Int
): Game<Boolean>(
    width,
    height,
    {_, _ -> Random.nextFloat() < 0.2},
    kernelMatrix,
) {
    override fun update(currentCell: Boolean, kernel: List<Boolean>): Boolean {
        val aliveNeighbours = kernel.fold(0) { acc, l -> if (l) acc + 1 else acc}
        return when (currentCell) {
            true -> aliveNeighbours in 2..3
            false -> aliveNeighbours == 3
        }
    }

    override fun render(imageData: Uint16Array) {
        for (i in 0..<this.data.size) {
            val di = i * 4
            val v = this.data[i]
            imageData[di] = if (v) 0xff else 0x00
            imageData[di + 1] =  if (v) 0xff else 0x00
            imageData[di + 2] =  if (v) 0xff else 0x00
            imageData[di + 3] =  0xff
        }
    }
}