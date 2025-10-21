# AI Prompts & Excerpts — Assignment 3

Below are several prompts I used with a generative AI assistant to redesign my ETL into a three-class object-oriented solution, together with short excerpts of the assistant’s responses. I adapted the suggestions to preserve exact A2 behavior.

## Prompt 1
Prompt: "Convert a one-file Java ETL into a minimal object-oriented design with only three public classes. Suggest class names and responsibilities while preserving input/output semantics."
Excerpt:*"Use `Product` (model), `CsvHandler` (read/write + simple parsing), and `ETLPipeline` (orchestrator). Keep `split(\",\", -1)` to preserve trailing empty fields."

## Prompt 2
Prompt: "How to apply a 10% discount using BigDecimal and round to two decimals HALF_UP?"
Excerpt: "Multiply by `new BigDecimal(\"0.9\")` then call `setScale(2, RoundingMode.HALF_UP)`."

## Prompt 3
Prompt: "How to detect a CSV header row if the first cell might be 'ProductID' or 'id'?"
Excerpt: "Check the first cell for strings like 'id', 'name', or 'product' (case-insensitive)."

## Prompt 4
Prompt: "How to preserve trailing empty fields when splitting a CSV line in Java?"
Excerpt: "Use `line.split(\",\", -1)`; negative limit preserves trailing empty tokens."

## How I adapted the AI
I used the AI suggestions as a starting point and edited code/javadoc to ensure the exact transformation rules and behavior from Assignment 2 are implemented precisely. I added row counters and the blank-line stopping logic to match Assignment 2.
