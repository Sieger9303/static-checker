import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.util.List;
public class Main
{
    public static String [] ruleNames = null;
    public static int decimalValue = -1;
    public static void main(String[] args) throws IOException{
        if (args.length > 0) {
            System.out.println(args[0]);
            String fileName = args[0];
            CharStream inputCharStream = CharStreams.fromFileName(fileName);
            SysYLexer sysYLexer = new SysYLexer(inputCharStream);
            CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
            SysYParser sysYParser = new SysYParser(tokens);
            ParseTree tree = sysYParser.program();
            // SemanticVisitor semanticVisitor = new SemanticVisitor();
            // semanticVisitor.visit(tree);
            checker staticChecker = new checker();
            staticChecker.visit(tree);
            // if (OutputHelper.ERROR_FLAG == false) {
            //     // ParserVisitor parserVisitor = new ParserVisitor();
            //     // parserVisitor.visit(tree);
            //     StaticChecker staticChecker = new StaticChecker();
            //     staticChecker.visit(tree);
            // }
        } else {
            System.err.println("We need an *.sy file!");
        }
    }

    public static String getTokenName(int tokenType) {
        if (tokenType < 1 || tokenType > ruleNames.length) {
            return "<INVALID>";
        }
        return ruleNames[tokenType - 1];
    }

    public static int getDecimalValue(int tokenType, String tokenValue) {
        if (tokenType != SysYLexer.INTEGER_CONST) {
            System.err.println("Illegal integer constant!");
        }
        if (tokenValue.startsWith("0x") || tokenValue.startsWith("0X")) {
            return Integer.parseInt(tokenValue.substring(2), 16);
        } else if (tokenValue.startsWith("0") && tokenValue.length() > 1) {
            return Integer.parseInt(tokenValue, 8);
        }
        return Integer.parseInt(tokenValue);
    }

    public static void printTokenInformation(Token token) {
        String tokenName = getTokenName(token.getType());
        String tokenText = token.getText();
        if (token.getType() == SysYLexer.INTEGER_CONST) {
            int decimalValue = getDecimalValue(token.getType(), tokenText);
            tokenText = Integer.toString(decimalValue);
        }
        System.err.println(tokenName + " " + tokenText + " at Line " + token.getLine() + ".");
    }
}
