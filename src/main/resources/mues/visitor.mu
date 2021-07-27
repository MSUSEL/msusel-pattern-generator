roles:
  - id: Element.Accept
    type: specific
    name: accept
  - id: Visitor.Visit
    type: roleBased
    dependsOn: ConcreteElement
    prefix: visit
