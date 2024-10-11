```marmaid
classDiagram

    ConvertUnit "unitA" --> "1" Unit
    ConvertUnit "unitB" --> "1" Unit

    Unit "1" <-- "unitRef" LocalizedUnit

    class ConvertUnit{
        - unitA: Unit
        - unitB: Unit
        - factor: double
    }

    class Unit{
        - id: int
        - description: String

        convert(other: Unit) double?
    }

    class LocalizedUnit{
        - id: int
        - unitRef: Unit
        - language: String

        - labelSg: String
        - labelSgAbrv: String

        - labelPl: String
        - labelPlAbrv: String

        convert(other: LocalizedUnit) double?
    }
```
