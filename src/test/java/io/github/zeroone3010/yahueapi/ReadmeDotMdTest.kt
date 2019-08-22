package io.github.zeroone3010.yahueapi

import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Test
import java.io.*
import java.net.URL
import java.util.*
import java.util.logging.Logger
import javax.tools.ToolProvider

internal class ReadmeDotMdTest {

  private enum class BlockType {
    CODE, IMPORTS, NONE
  }

  @Ignore
  @Test
  @Throws(IOException::class)
  fun verifyExamplesOnReadmeDotMdCanBeCompiled() {
    val logger = Logger.getAnonymousLogger()

    val readmeDotMdLocation = ClassLoader.getSystemResource("README.md")
    Assumptions.assumeTrue(readmeDotMdLocation != null, "README.md not found.")

    logger.info("Parsing code blocks from README.md")
    val code = readReadmeDotMdCodeBlocks(readmeDotMdLocation!!)

    logger.info("Found " + code.codeBlocks.size + " blocks, starting to compile.")
    assertAllCodeBlocksCanBeCompiled(code)

    logger.info("Successfully compiled all code blocks.")
  }

  @Throws(IOException::class)
  private fun readReadmeDotMdCodeBlocks(readmeDotMdLocation: URL): Code {
    val sb = StringBuilder()
    var blockType = BlockType.NONE
    var codeHeader: String? = null
    var codeBlockNumber = 0
    var checkedExceptions: String? = null

    val lines = BufferedReader(InputStreamReader(readmeDotMdLocation.openStream())).readLines()
    val codeBlocks = LinkedHashMap<String, String>()
    val importStatements = ArrayList<String>()
    for (line in lines) {
      if (line.startsWith(COMMENT_LINE_START)) {
        codeHeader = line.replace(COMMENT_LINE_START, "").replace(COMMENT_LINE_END, "")
      } else if (line == CODE_BLOCK_STARTS) {
        if (CODE_IMPORTS == codeHeader) {
          blockType = BlockType.IMPORTS
        } else {
          blockType = BlockType.CODE
        }
        if (codeHeader != null && codeHeader.startsWith(CODE_REQUIRES)) {
          val requiredBlock = codeHeader.replace(CODE_REQUIRES, "")
          sb.append(codeBlocks[requiredBlock])
          codeHeader = null
        }
        if (codeHeader != null && codeHeader.startsWith(CODE_THROWS)) {
          sb.append("try {")
          checkedExceptions = codeHeader.replace(CODE_THROWS, "")
          codeHeader = null
        }
        if (codeHeader == null) {
          codeHeader = codeBlockNumber.toString()
          codeBlockNumber++
        }
      } else if (line == CODE_BLOCK_ENDS && codeHeader != null) {
        if (checkedExceptions != null) {
          sb.append("} catch(").append(checkedExceptions).append(" e) {/* Ignoring on purpose */}")
        }
        codeBlocks[codeHeader] = sb.toString()
        blockType = BlockType.NONE
        codeHeader = null
        checkedExceptions = null
        sb.setLength(0)
      } else if (blockType == BlockType.CODE) {
        sb.append(line).append("\n")
      } else if (blockType == BlockType.IMPORTS) {
        importStatements.add(line)
      }
    }
    return Code(importStatements, codeBlocks)
  }

  @Throws(IOException::class)
  private fun assertAllCodeBlocksCanBeCompiled(code: Code) {
    for ((header, block) in code.codeBlocks) {

      val javaFile = File.createTempFile("ReadmeDotMdCodeBlock", ".java")
      val writer = FileWriter(javaFile)
      for (importStatement in code.importStatements) {
        writer.write(importStatement)
      }
      writer.write("")
      writer.write("public class " + javaFile.name.replace(".java", "") + " {\n")
      writer.write("  public static void main(String[] args) {\n")
      writer.write(block)
      writer.write("  }\n")
      writer.write("}\n")
      writer.flush()
      writer.close()

      val javaCompiler = ToolProvider.getSystemJavaCompiler()
      val result = javaCompiler.run(null, null, null, javaFile.absolutePath)
      assertEquals(0, result, "Code block '$header' fails to compile: \n$block")
    }
  }


  private class Code(val importStatements: List<String>, val codeBlocks: Map<String, String>)

  companion object {
    private val COMMENT_LINE_START = "[//]: # ("
    private val COMMENT_LINE_END = ")"
    private val CODE_REQUIRES = "requires-"
    private val CODE_THROWS = "throws-"
    private val CODE_IMPORTS = "imports"
    private val CODE_BLOCK_STARTS = "```java"
    private val CODE_BLOCK_ENDS = "```"
  }
}
