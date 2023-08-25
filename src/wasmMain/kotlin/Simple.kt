import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

fun main() {
    val size = 1000
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.width = size
    canvas.height = size

    document.body!!.append(canvas)

    val gol = GameOfLife(canvas.width, canvas.height)
    val runner = Runner(gol, canvas.getContext("2d") as CanvasRenderingContext2D)
    runner.start()
}
