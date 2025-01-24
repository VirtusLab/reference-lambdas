package model

type Foo struct {
	Str string `json:"str"`
}

type Bar struct {
	Foo Foo `json:"foo"`
}

type Baz struct {
	Str string `json:"str"`
}
