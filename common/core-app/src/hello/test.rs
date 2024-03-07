use std::collections::HashMap;
use std::iter::once;
use std::sync::Arc;
use uniffi;

#[derive(uniffi::Object)]
pub struct Test {
    name: String,
}

#[uniffi::export]
impl Test {
    #[uniffi::constructor]
    pub fn new(name: String) -> Arc<Self> {
        Arc::new(Test { name })
    }
    pub fn say(&self) -> String {
        return self.name.clone();
    }
}
